package shop.geeksasang.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.geeksasang.config.domain.BaseEntity;
import shop.geeksasang.config.domain.LoginStatus;
import shop.geeksasang.config.domain.Status;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private int id;

    private String loginId;

    private String nickName;

    private String email;

    private String password;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="university_id")
    private University university;

    private String phoneNumber;

    private String phoneValidKey;

    private String phoneValidStatus;

    private String profileImgUrl;

    private String emailValidKey;

    private String emailValidStatus;

    private String jwtToken;

    private String informationAgreeStatus; // 회원 정보 동의 여부

    @Enumerated(EnumType.STRING)
    private LoginStatus loginStatus; // 첫 번째 로그인인지 아닌지

    public void changeStatusToActive(){
        super.setStatus(Status.ACTIVE);
    }

    //-// 연관 관계 편의 메서드 //-//
    // 대학교 정보 저장
    public void connectUniversity(University university){
        this.university = university;
    }

    // 수정: 폰번호 저장
    public void updatePhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    //수정: 폰 인증번호 수정
    public void updatePhoneValidKey(String phoneValidKey){
        this.phoneValidKey = phoneValidKey;
    }

    // 수정: 폰 번호
    public void updateProfileImgUrl(String profileImgUrl){
        this.profileImgUrl = profileImgUrl;
    }

    // 수정: 회원정보 동의 수정
    public void updateInformationAgreeStatus(String informationAgreeStatus){
        this.informationAgreeStatus = informationAgreeStatus;
    }

    // 값 확인용 메서드
    public void updateNickname(String nickName) { this.nickName = nickName; }

    public void updatePassword(String password) { this.password = password; }


    // 회원 탈퇴
    public void changeStatusToInactive(){
        super.setStatus(Status.INACTIVE);
    }

    // 로그인 안해본 디폴트 저장
    public void changeLoginStatusToNever(){
        this.loginStatus = LoginStatus.NEVER;
    }

    // 로그인 횟수 상태 첫번째 초과 저장
    public void changeLoginStatusToNotNever(){
        this.loginStatus = LoginStatus.NOTNEVER;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", university=" + university +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneValidKey='" + phoneValidKey + '\'' +
                ", phoneValidStatus='" + phoneValidStatus + '\'' +
                ", profileImgUrl='" + profileImgUrl + '\'' +
                ", emailValidKey='" + emailValidKey + '\'' +
                ", emailValidStatus='" + emailValidStatus + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
