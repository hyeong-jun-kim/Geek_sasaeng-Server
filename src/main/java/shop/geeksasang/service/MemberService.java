package shop.geeksasang.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.geeksasang.config.exception.BaseException;
import shop.geeksasang.domain.Member;
import shop.geeksasang.domain.University;

import shop.geeksasang.dto.member.*;
import shop.geeksasang.repository.MemberRepository;
import shop.geeksasang.repository.UniversityRepository;
import shop.geeksasang.utils.jwt.RedisUtil;
import shop.geeksasang.utils.encrypt.SHA256;

import java.util.Optional;

import static shop.geeksasang.config.exception.BaseResponseStatus.*;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;
    private final RedisUtil redisUtil;

    private final long expireTime = 60 * 5L; // 이메일 유효 기간

    @Transactional(readOnly = false)
    public Member createMember(CreateMemberReq dto){

         if(!dto.getCheckPassword().equals(dto.getPassword())) {
             throw new BaseException(DIFFRENT_PASSWORDS);
         }

        if(!memberRepository.findMemberByLoginId(dto.getLoginId()).isEmpty()){
            throw new BaseException(DUPLICATE_USER_LOGIN_ID);
        }

        if(!memberRepository.findMemberByEmail(dto.getEmail()).isEmpty()){
            throw new BaseException(DUPLICATE_USER_EMAIL);
        }

        // 검증: 동의여부가 Y 가 이닌 경우
        if(!dto.getInformationAgreeStatus().equals("Y")){
            throw new BaseException(INVALID_INFORMATIONAGREE_STATUS);
        }

        dto.setPassword(SHA256.encrypt(dto.getPassword()));
        Member member = dto.toEntity();
        University university = universityRepository
                .findUniversitiesByName(dto.getUniversityName())
                .orElseThrow(() -> new BaseException(NOT_EXISTS_UNIVERSITY));

        member.connectUniversity(university);
        member.changeStatusToActive();
        memberRepository.save(member);
        return member;
    }


    // 수정: 폰 번호
    @Transactional(readOnly = false) // readOnly = false : 생성, 수정하는 작업에 적용
    public Member updateMemberPhoneNumber(int id, PatchMemberPhoneNumberReq dto){

        // 멤버 아이디로 조회
        Member findMember = memberRepository
                .findById(id)
                .orElseThrow(() -> new BaseException(INTERNAL_SERVER_ERROR));
        // 폰 번호 수정
        findMember.updatePhoneNumber(dto.getPhoneNumber());

        return findMember;
    }

    // 수정: 폰 인증 번호
    @Transactional(readOnly = false)
    public Member updateMemberPhoneValidKey(int id, PatchMemberPhoneValidKeyReq dto){

        //멤버 아이디로 조회
        Member findMember = memberRepository
                .findById(id)
                .orElseThrow(()-> new BaseException(INTERNAL_SERVER_ERROR));
        //폰 인증번호 수정
        findMember.updatePhoneValidKey(dto.getPhoneValidKey());

        return findMember;
    }

    // 수정: 프로필 이미지
    @Transactional(readOnly = false)
    public Member updateProfileImgUrl(int id, PatchProfileImgUrlReq dto){

        //멤버 아이디로 조회
        Member findMember = memberRepository
                .findById(id)
                .orElseThrow(() -> new BaseException(INTERNAL_SERVER_ERROR));
        //프로필 이미지 수정
        findMember.updateProfileImgUrl(dto.getProfileImgUrl());

        return findMember;
    }

    // 수정: 회원정보 동의 수정
    public Member updateInformationAgreeStatus(int id, PatchInformationAgreeStatusReq dto){

        //멤버 아이디로 조회
        Member findMember = memberRepository
                .findById(id)
                .orElseThrow(()-> new BaseException(INTERNAL_SERVER_ERROR));
        //동의 여부 수정
        findMember.updateInformationAgreeStatus(dto.getInformationAgreeStatus());

        return findMember;
    }

}
