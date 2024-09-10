package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.EncryptUtil;
import lombok.Builder;

import java.io.Serializable;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserInfoDTO(
        @NotBlank(message = "아이디는 필수 입력 사항입니다.")
        @Size(min = 4, max = 16, message = "아이디는 최소 4글자에서 16글자까지 입력가능합니다.")
        String userId,
        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        @Size(max = 10, message = "아이디는 10글자까지 입력가능합니다.")
        String userName,
        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        @Size(max = 16, message = "비밀번호는 16글자까지 입력가능합니다.")
        String password,
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        @Size(max = 30, message = "이메일은 30글자까지 입력가능합니다.")
        String email,
        @NotBlank(message = "주소는 필수 입력 사항입니다.")
        @Size(max = 30, message = "주소는 30글자까지 입력가능합니다.")
        String addr1,
        @NotBlank(message = "상세 주소는 필수 입력 사항입니다.")
        @Size(max = 100, message = "상세 주소는 100글자까지 입력가능합니다.")
        String addr2,
        
        String regId,
        String regDt,
        String chgId,
        String chgDt,
        String roles,
        String existYn
) implements Serializable {
        /*
        JPA로 전달받은 entity 결과를 DTO로 변환한기
        이전 실습에서 진행한 Jackson 객체를 통해서도 가능
         */
        public static UserInfoDTO createUser(UserInfoDTO pDTO, String password, String roles) throws Exception {
                UserInfoDTO rDTO = UserInfoDTO.builder()
                        .userId(pDTO.userId)
                        .userName(pDTO.userName)
                        .password(password)
                        .email(EncryptUtil.encAES128CBC(pDTO.email))
                        .addr1(pDTO.addr1)
                        .addr2(pDTO.addr2)
                        .roles(roles)
                        .regId(pDTO.userId)
                        .regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                        .chgId(pDTO.userId)
                        .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                        .build();

                return rDTO;
        }
        public static UserInfoEntity of(UserInfoDTO dto){
                UserInfoEntity entity = UserInfoEntity.builder()
                        .userId(dto.userId)
                        .userName(dto.userName)
                        .password(dto.password)
                        .email(dto.email)
                        .addr1(dto.addr1)
                        .addr2(dto.addr2)
                        .roles(dto.roles)
                        .regId(dto.regId)
                        .regDt(dto.regDt)
                        .chgId(dto.chgId)
                        .chgDt(dto.chgDt)
                        .build();
                return entity;
        }
        /*
        DTO를 entity로 변환
         */
        public static UserInfoDTO from(UserInfoEntity entity) throws Exception {

                UserInfoDTO dto = UserInfoDTO.builder()
                        .userId(entity.getUserId())
                        .userName(entity.getUserName())
                        .password(entity.getPassword())
                        .email(EncryptUtil.decAES128CBC(CmmUtil.nvl(entity.getEmail())))
                        .addr1(entity.getAddr1())
                        .addr2(entity.getAddr2())
                        .regId(entity.getRegId())
                        .regDt(entity.getRegDt())
                        .chgId(entity.getChgId())
                        .chgDt(entity.getChgDt())
                        .build();

                return dto;
        }
}
