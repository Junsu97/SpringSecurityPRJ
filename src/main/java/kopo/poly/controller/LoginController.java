package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.auth.AuthInfo;
import kopo.poly.controller.response.CommonResponse;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequestMapping(value = "/login/v1")
@RequiredArgsConstructor
@RestController
public class LoginController {
    @PostMapping(value = "loginSuccess")
    public ResponseEntity<CommonResponse> loginSuccess(@AuthenticationPrincipal AuthInfo authInfo, HttpSession session) {
        log.info(this.getClass().getName() + ".loginSuccess Start!!!!");

        UserInfoDTO rDTO = Optional.ofNullable(authInfo.getUserInfoDTO()).orElseGet(() -> UserInfoDTO.builder().build());

        String userId = CmmUtil.nvl(rDTO.userId());
        String userName = CmmUtil.nvl(rDTO.userName());
        String userRoles = CmmUtil.nvl(rDTO.roles());

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("userRoles : " + userRoles);

        session.setAttribute("SS_USER_ID", userId);
        session.setAttribute("SS_USER_NAME", userName);
        session.setAttribute("SS_USER_ROLES", userRoles);

        MsgDTO dto = MsgDTO.builder().result(1).msg(userName + "님 로그인 성공하였습니다.").build();

        log.info(this.getClass().getName() + ".loginSuccess End!!!!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto)
        );
    }

    @PostMapping(value = "loginFail")
    public ResponseEntity<CommonResponse> loginFail() {
        log.info(this.getClass().getName() + ".loginFail Start!!!!");
        MsgDTO dto = MsgDTO.builder().result(1).msg("로그인이 실패하였습니다.").build();
        log.error(this.getClass().getName() + "loginFail End!!!!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto)
        );
    }

    @PostMapping(value = "loginInfo")
    public ResponseEntity<CommonResponse> loginInfo(HttpSession session) {
        log.info(this.getClass().getName() + ".loginInfo Start!!!!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String userName = CmmUtil.nvl((String) session.getAttribute("SS_USER_NAME"));
        String userRoles = CmmUtil.nvl((String) session.getAttribute("SS_USER_ROLES"));

        UserInfoDTO dto = UserInfoDTO.builder().userId(userId).userName(userName).roles(userRoles).build();
        log.info(this.getClass().getName() + ".loginInfo End!!!!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto)
        );
    }
}
