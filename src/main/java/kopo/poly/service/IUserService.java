package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;
    int insertUserInfo(UserInfoDTO pDTO);
    UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception;

}
