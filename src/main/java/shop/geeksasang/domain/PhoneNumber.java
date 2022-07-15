package shop.geeksasang.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.geeksasang.config.domain.BaseEntity;
import shop.geeksasang.config.domain.ValidStatus;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
public class PhoneNumber extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="phoneNumber_id")
    private int id;

    private String number;

    @Enumerated(EnumType.STRING)
    private ValidStatus phoneValidStatus;

    //-// 연관 관계 편의 메서드 //-//
    // 핸드폰 인증 성공
    public void changePhoneValidStatusToSuccess() {
        this.phoneValidStatus = ValidStatus.SUCCESS;
    }
}
