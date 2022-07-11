package shop.geeksasang.dto.member;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shop.geeksasang.domain.Member;

@Getter @Setter
@Builder
public class PostRegisterRes {

    @ApiModelProperty(example = "geeksasaeng")
    @ApiParam(value = "사용자 ID")
    private  String loginId;

    @ApiModelProperty(example = "긱사생")
    @ApiParam(value = "사용자 닉네임")
    private  String nickname;

    @ApiModelProperty(example = "Gachon University")
    @ApiParam(value = "사용자 대학교")
    private  String universityName;

    @ApiModelProperty(example = "abc@gachon.ac.kr")
    @ApiParam(value = "사용자 이메일")
    private  String email;

    @ApiModelProperty(example = "01012341234")
    @ApiParam(value = "사용자 핸드폰 번호")
    private  String phoneNumber;

    static public PostRegisterRes toDto(Member member) {
        return PostRegisterRes.builder()
                .loginId(member.getLoginId())
                .nickname(member.getNickName())
                .universityName(member.getUniversity().getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}
