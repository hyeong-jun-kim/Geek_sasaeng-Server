package shop.geeksasang.dto.deliveryPartyMember;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.geeksasang.domain.DeliveryPartyMember;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDeliveryPartyMemberReq {

//    @ApiModelProperty(example = "1")
//    @ApiParam(value = "멤버 id", required = true)
//    @NotNull
//    private int participantId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "파티 id", required = true)
    @NotNull
    private int partyId;

    public DeliveryPartyMember toEntity() {
        return DeliveryPartyMember.builder()
                .build();
    }

}
