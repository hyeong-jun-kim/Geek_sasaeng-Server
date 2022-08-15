package shop.geeksasang.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.geeksasang.config.exception.BaseException;
import shop.geeksasang.config.exception.response.BaseResponseStatus;
import shop.geeksasang.domain.Announcement;
import shop.geeksasang.domain.DeliveryPartyMember;
import shop.geeksasang.domain.Member;
import shop.geeksasang.dto.announcement.get.GetAnnouncementRes;
import shop.geeksasang.dto.chatroom.GetChatRoomsRes;
import shop.geeksasang.dto.commercial.GetCommercialsRes;
import shop.geeksasang.dto.deliveryParty.get.GetDeliveryPartiesRes;
import shop.geeksasang.dto.login.JwtInfo;
import shop.geeksasang.repository.AnnouncementRepository;
import shop.geeksasang.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final MemberRepository memberRepository;

    public List<GetAnnouncementRes> getAnnouncements(JwtInfo jwtInfo){

        int chiefId = jwtInfo.getUserId();

        //요청 보낸 사용자 Member 조회
        int memberId = jwtInfo.getUserId();
        Member findMember = memberRepository.findMemberByIdAndStatus(memberId).
                orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_EXISTS_PARTICIPANT));

        //저장된 announcements 조회
        List<Announcement> announcements = announcementRepository.findAnnouncementOrderByCreatedAt();

        //dto로 변경
        List<GetAnnouncementRes> results = announcementRepository.findAnnouncementOrderByCreatedAt()
                .stream().map(announcement -> GetAnnouncementRes.toDto(announcement))
                .collect(Collectors.toList());

        return results;

    }
}
