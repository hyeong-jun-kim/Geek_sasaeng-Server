package shop.geeksasang.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.geeksasang.config.domain.OrderTimeCategoryType;
import shop.geeksasang.config.exception.BaseException;
import shop.geeksasang.config.exception.BaseResponseStatus;
import shop.geeksasang.config.response.BaseResponse;
import shop.geeksasang.dto.deliveryParty.*;
import shop.geeksasang.dto.login.JwtInfo;
import shop.geeksasang.service.DeliveryPartyService;
import shop.geeksasang.utils.jwt.NoIntercept;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static shop.geeksasang.config.exception.BaseResponseStatus.NOT_EXISTS_ORDER_TIME_CATEGORY;

@RestController
@RequiredArgsConstructor // final로 선언 된 것 자동으로 @Autowired와 같은 기능
public class DeliveryPartyController {

    private final DeliveryPartyService deliveryPartyService;

    //배달 파티 생성
    @ApiOperation(value = "배달 파티 생성", notes = "사용자는 배달 파티를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(code =1000 ,message ="요청에 성공하였습니다"),
            @ApiResponse(code =2009 ,message ="존재하지 않는 멤버입니다"),
            @ApiResponse(code =2606 ,message ="기숙사가 존재하지 않습니다"),
            @ApiResponse(code =2402 ,message ="존재하지 않는 카테고리입니다"),
            @ApiResponse(code =4000 ,message = "서버 오류입니다.")
    })
    @PostMapping("/delivery-party")
    public BaseResponse<PostDeliveryPartyRes> registerDeliveryParty(@Validated @RequestBody PostDeliveryPartyReq dto,  HttpServletRequest request){
        JwtInfo jwtInfo = (JwtInfo) request.getAttribute("jwtInfo");

        PostDeliveryPartyRes postDeliveryPartyRes = deliveryPartyService.registerDeliveryParty(dto, jwtInfo);
        return new BaseResponse<>(postDeliveryPartyRes);
    }

    //배달파티 조회: 전체목록
    @ApiOperation(value = "전체 배달파티 조회", notes = "cursor은 0부터 시작. dormitoryId는 현재 대학교 id. 예시 : https://geeksasaeng.shop/1/delivery-parties?cursor=0  ")
    @ApiResponses({
            @ApiResponse(code =1000 ,message ="요청에 성공하셨습니다."),
            @ApiResponse(code=4000, message = "서버 오류입니다.")
    })
    @NoIntercept
    @GetMapping("/{dormitoryId}/delivery-parties")
    public BaseResponse<List<GetDeliveryPartiesRes>> getAllDeliveryParty(@PathVariable int dormitoryId, @RequestParam int cursor){
        List<GetDeliveryPartiesRes> response = deliveryPartyService.getDeliveryPartiesByDormitoryId(dormitoryId, cursor);
        return new BaseResponse<>(response);
    }


    //배달파티 조회: 상세조회
    @NoIntercept
    @ApiOperation(value = "조회: 배달파티 상세조회", notes = "배달파티 게시물을 선택하면 상세 정보들을 볼 수 있다.")
    @ApiResponses({
            @ApiResponse(code =1000 ,message ="요청에 성공하셨습니다."),
            @ApiResponse(code = 2010, message = "존재하지 않는 파티입니다."),
            @ApiResponse(code=4000,message = "서버 오류입니다.")
    })
    @GetMapping("/delivery-party/{partyId}")
    public BaseResponse<GetDeliveryPartyDetailRes> getDeliveryPartyDetailById(@PathVariable("partyId") int partyId){
        GetDeliveryPartyDetailRes response = deliveryPartyService.getDeliveryPartyDetailById(partyId);

        return new BaseResponse<>(response);
    }

    // 배달파티 조회: 인원수
    @ApiOperation(value = "조회 : 배달파티 목록 인원수에 따라 조회", notes = "해당 기숙사의 배달파티 목록을 인원수에 따라 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(code =1000 ,message ="요청에 성공하였습니다."),
            @ApiResponse(code =2604 ,message ="지정된 값이 아닙니다"),
            @ApiResponse(code=4000,message = "서버 오류입니다.")
    })
    @NoIntercept
    @GetMapping("/{dormitoryId}/delivery-parties/{maxMatching}")
    public BaseResponse<List<GetDeliveryPartyByMaxMatchingRes>> getDeliveryPartyByMaxMatching(@PathVariable int dormitoryId, @PathVariable int maxMatching, @RequestParam("cursor") int cursor){
        List<GetDeliveryPartyByMaxMatchingRes> response = deliveryPartyService.getDeliveryPartyByMaxMatching(dormitoryId, maxMatching, cursor);
        return new BaseResponse<>(response);
    }

    // 배달파티 조회: orderTimeType
    @ApiOperation(value = "조회 : 배달파티 목록 주문 시간대에 따라 조회", notes = "해당 기숙사의 배달파티 목록을 주문 시간대에 따라 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(code =1000 ,message ="요청에 성공하였습니다."),
            @ApiResponse(code =2608 ,message ="존재하지 않는 시간 카테고리 입니다"),
            @ApiResponse(code=4000,message = "서버 오류입니다.")
    })
    @NoIntercept
    @GetMapping("/{dormitoryId}/delivery-parties/filter/{orderTimeCategory}")
    public BaseResponse<List<GetDeliveryPartyByOrderTimeRes>> GetDeliveryPartyByOrderTime(@PathVariable int dormitoryId, @PathVariable String orderTimeCategory, @RequestParam("cursor") int cursor){
        // enum값 아닌 것 들어올때 처리 - 리팩토링 대상
        try{
            System.out.println(OrderTimeCategoryType.valueOf(orderTimeCategory));
        }catch(IllegalArgumentException e){
            throw new BaseException(NOT_EXISTS_ORDER_TIME_CATEGORY);
        }
        List<GetDeliveryPartyByOrderTimeRes> response = deliveryPartyService.getDeliveryPartyByOrderTime(dormitoryId, cursor, orderTimeCategory);
        return new BaseResponse<>(response);
    }


    //배달파티 조회: 검색어로 조회
    @ApiOperation(value = "조회 : 검색어를 포함하는 배달파티 목록 조회", notes = "해당 기숙사의 배달파티 목록울 검색어로 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(code =1000 ,message ="요청에 성공하였습니다."),
            @ApiResponse(code=2205,message = "검색어를 입력해주세요"),
            @ApiResponse(code=4000,message = "서버 오류입니다.")
    })
    @NoIntercept // jwt 필요 없음
    @GetMapping("/{dormitoryId}/delivery-parties/keyword/{keyword}")
    public BaseResponse<List<GetDeliveryPartiesByKeywordRes>> getDeliveryPartiesByKeyword(@PathVariable("dormitoryId") int dormitoryId, @PathVariable("keyword") String keyword,@RequestParam int cursor){
        List<GetDeliveryPartiesByKeywordRes> response = deliveryPartyService.getDeliveryPartiesByKeyword(dormitoryId, keyword, cursor);
        return new BaseResponse<>(response);
    }

}