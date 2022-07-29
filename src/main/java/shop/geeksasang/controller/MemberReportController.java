package shop.geeksasang.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shop.geeksasang.config.response.BaseResponse;
import shop.geeksasang.dto.login.JwtInfo;
import shop.geeksasang.dto.report.PostMemberReportRegisterReq;
import shop.geeksasang.service.BlockService;
import shop.geeksasang.service.MemberReportService;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/reports/members")
@RestController
@RequiredArgsConstructor
public class MemberReportController {

    private final MemberReportService memberReportService;
    private final BlockService blockService;

    public static final String SUCCESS_MESSAGE = "신고 생성에 성공하셨습니다";

    @ApiOperation(value = "생성: 멤버 신고 생성", notes = "멤버 신고를 생성한다. 성공하면 성공 메세지만 리턴합니다.")
    @ApiResponses({
            @ApiResponse(code =1000 ,message ="요청에 성공하셨습니다."),
            @ApiResponse(code =2017 ,message ="하루 신고 최대 횟수를 초과하셨습니다."),
            @ApiResponse(code =2018 ,message ="중복 신고는 불가능합니다."),
            @ApiResponse(code =2019 ,message ="존재하지 않는 신고 카테고리입니다"),
            @ApiResponse(code =2204 ,message ="존재하지 않는 회원 id 입니다."),
            @ApiResponse(code=4000,message = "서버 오류입니다.")
    })
    @PostMapping()
    public BaseResponse<String> registerMemberReport(@Validated @RequestBody PostMemberReportRegisterReq dto, HttpServletRequest request){
        JwtInfo jwtInfo = (JwtInfo) request.getAttribute("jwtInfo");
        memberReportService.registerMemberReport(dto, jwtInfo);

        if(dto.isBlock()){
            blockService.block(jwtInfo.getUserId(), dto.getReportedMemberId());
        }

        return new BaseResponse<>(SUCCESS_MESSAGE);
    }
}
