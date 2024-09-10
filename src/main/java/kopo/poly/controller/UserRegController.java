package kopo.poly.controller;

import jakarta.validation.Valid;
import kopo.poly.auth.UserRole;
import kopo.poly.controller.response.CommonResponse;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/reg/v1")
@RequiredArgsConstructor
@RestController
public class UserRegController {
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "getUserIdExists")
    public ResponseEntity<CommonResponse> getUserIdExists(@RequestBody UserInfoDTO pDTO) throws Exception{
        log.info(this.getClass().getName() + ".getUserIdExists Start!!!");

        UserInfoDTO rDTO = userService.getUserIdExists(pDTO);

        log.info(this.getClass().getName() + ".getUserIdExists End!!!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rDTO)
        );
    }

    @PostMapping(value = "insertUserInfo")
    public ResponseEntity<CommonResponse> insertUserInfo(@Valid @RequestBody UserInfoDTO pDTO,
                                                         BindingResult bindingResult) {
        log.info(this.getClass().getName() + ".insertUserInfo Start!!!");
        if(bindingResult.hasErrors()) {
            return CommonResponse.getErrors(bindingResult);
        }

        int res = 0;
        String msg = "";
        MsgDTO dto;

        log.info("pDTO : " + pDTO);

        try{
            UserInfoDTO nDTO = UserInfoDTO.createUser(
                    pDTO, passwordEncoder.encode(pDTO.password()), UserRole.USER.getRole()
            );

            res = userService.insertUserInfo(nDTO);

            log.info("회원가입 결과(res) : " +  res);
            if(res == 1){
                msg = "회원가입되었습니다.";
            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";
            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
            }
        }catch (Exception e){
            msg = "실패하였습니다." + e;
            res = 2;
            log.info(e.toString());
        } finally {
            dto = MsgDTO.builder().result(res).msg(msg).build();
            log.info(this.getClass().getName() + ".insertUserInfo End!!!");
        }

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto)
        );
    }
}
