package shop.geeksasang.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.geeksasang.config.domain.OrderTimeCategoryType;
import shop.geeksasang.config.exception.BaseException;
import shop.geeksasang.config.exception.BaseResponseStatus;
import shop.geeksasang.domain.*;
import shop.geeksasang.dto.deliveryParty.GetDeliveryPartiesRes;
import shop.geeksasang.dto.deliveryParty.GetDeliveryPartyByMaxMatchingRes;
import shop.geeksasang.dto.deliveryParty.GetDeliveryPartyByOrderTimeRes;
import shop.geeksasang.dto.deliveryParty.GetDeliveryPartyDetailRes;
import shop.geeksasang.dto.deliveryParty.PostDeliveryPartyReq;
import shop.geeksasang.repository.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static shop.geeksasang.config.exception.BaseResponseStatus.NOT_SPECIFIED_VALUE;


@Transactional
@Service
@RequiredArgsConstructor // final로 선언 된 것 자동으로 @Autowired와 같은 기능
public class DeliveryPartyService {

    private final DeliveryPartyRepository deliveryPartyRepository;
    private final MemberRepository memberRepository;
    private final DormitoryRepository dormitoryRepository;
    private final HashTagRepository hashTagRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final DeliveryPartyHashTagRepository deliveryPartyHashTagRepository;

    private static final int PAGING_SIZE = 10;
    private static final String PAGING_STANDARD = "orderTime";
    private static final List<Integer> MATHCING_NUMBER = Arrays.asList(2, 4, 6, 8, 10);


    @Transactional(readOnly = false) // ?
    public DeliveryParty registerDeliveryParty(PostDeliveryPartyReq dto){

        // 파티 생성 및 저장
        DeliveryParty deliveryParty = dto.toEntity();

        //파티장
        Member chief = memberRepository.findById(dto.getChief())
                .orElseThrow(() -> new RuntimeException(""));
        deliveryParty.connectChief(chief);

        //도미토리
        Dormitory dormitory = dormitoryRepository.findById(dto.getDormitory())
                .orElseThrow(() -> new RuntimeException(""));
        deliveryParty.connectDormitory(dormitory);

        //카테고리
        FoodCategory foodCategory = foodCategoryRepository.findById(dto.getFoodCategory())
                .orElseThrow(() -> new RuntimeException(""));
        deliveryParty.connectFoodCategory(foodCategory);

        deliveryPartyRepository.save(deliveryParty);

        // 반환
        return deliveryParty;
    }

    //배달파티 조회: 기숙사 별 전체목록
    public List<GetDeliveryPartiesRes> getDeliveryPartiesByDormitoryId(int dormitoryId, int cursor){

        PageRequest paging = PageRequest.of(cursor, PAGING_SIZE, Sort.by(Sort.Direction.ASC, PAGING_STANDARD ));

        Slice<DeliveryParty> deliveryParties = deliveryPartyRepository.findDeliveryPartiesByDormitoryId(dormitoryId, paging);

        return deliveryParties.stream()
                .map(deliveryParty -> GetDeliveryPartiesRes.toDto(deliveryParty))
                .collect(Collectors.toList());
    }

//    //배달파티 상세조회:
//    public DeliveryParty getDeliveryParty(int partyId){
//        DeliveryParty deliveryParty= deliveryPartyRepository.findById(partyId)
//                .orElseThrow(() -> new RuntimeException(""));
//        return deliveryParty;
//    }

    //배달파티 상세조회:
    public GetDeliveryPartyDetailRes getDeliveryPartyDetailById(int partyId){
        DeliveryParty deliveryParty = deliveryPartyRepository.findById(partyId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_EXISTS_PARTY));

        GetDeliveryPartyDetailRes getDeliveryPartyDetailRes = GetDeliveryPartyDetailRes.toDto(deliveryParty);
        return getDeliveryPartyDetailRes;
    }


    // 배달파티 조회: 인원수
    public List<GetDeliveryPartyByMaxMatchingRes> getDeliveryPartyByMaxMatching(int dormitoryId, int maxMatching, int cursor) {

        // 인원수 입렵값 validation
        if(!MATHCING_NUMBER.contains(maxMatching)){
            throw new BaseException(NOT_SPECIFIED_VALUE);
        }

        PageRequest paging = PageRequest.of(cursor, PAGING_SIZE, Sort.by(Sort.Direction.ASC, PAGING_STANDARD));

        Slice<DeliveryParty> deliveryParties = deliveryPartyRepository.findDeliveryPartiesByMaxMatching(dormitoryId, maxMatching, paging);

        return deliveryParties.stream()
                .map(deliveryParty -> GetDeliveryPartyByMaxMatchingRes.toDto(deliveryParty))
                .collect(Collectors.toList());
    }

    // 배달파티 조회: orderTimeCategory 시간대
    public List<GetDeliveryPartyByOrderTimeRes> getDeliveryPartyByOrderTime(int dormitoryId, int cursor, String orderTimeCategory) {

        OrderTimeCategoryType orderTimeCategoryType = OrderTimeCategoryType.valueOf(orderTimeCategory);

        PageRequest paging = PageRequest.of(cursor, PAGING_SIZE, Sort.by(Sort.Direction.ASC, PAGING_STANDARD));

        Slice<DeliveryParty> deliveryParties = deliveryPartyRepository.findDeliveryPartiesByOrderTime(dormitoryId, orderTimeCategoryType, paging);

        return deliveryParties.stream()
                .map(deliveryParty -> GetDeliveryPartyByOrderTimeRes.toDto(deliveryParty))
                .collect(Collectors.toList());
    }

}