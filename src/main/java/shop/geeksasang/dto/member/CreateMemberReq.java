package shop.geeksasang.dto.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import shop.geeksasang.domain.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMemberReq {

    @ApiModelProperty(value = "로그인 아이디, 최소 6자") // swagger 용 어노테이션
    @Size(min = 6) // validation: 최소길이 6자
    private  String loginId;

    @ApiModelProperty(value = "비밀번호, 최소 8자")
    @Size(min = 8)
    private  String password;

    @ApiModelProperty(value = "비밀번호 확인용, 최소 8자")
    @Size(min = 8)
    private  String checkPassword;

    @ApiModelProperty(value = "닉네임, 최소 6자")
    @Size(min = 6)
    private  String nickname;

    @ApiModelProperty(value = "대학 이름, 해커톤에서는 'Gachon University'를 사용")
    @NotBlank
    private  String universityName;

    @ApiModelProperty(value = "이메일을 사용")
    @Email
    private  String email;

    @ApiModelProperty(value = "휴대폰 번호만 입력. 최소 입력 10, 최대 11")
    @Size(min = 10, max = 11)
    private  String phoneNumber;

    @ApiModelProperty(value = "회원 정보동의 여부 입력, Null, 빈 문자열, 스페이스만 있는 문자열 불가")
    @NotBlank //Null, 빈 문자열, 스페이스만 있는 문자열 불가
    private String informationAgreeStatus;

    public Member toEntity() {
        return Member.builder()
                .loginId(getLoginId())
                .password(getPassword())
                .nickName(getNickname())
                .email(getEmail())
                .phoneNumber(getPhoneNumber())
                .informationAgreeStatus(getInformationAgreeStatus())
                .build();
    }
}








