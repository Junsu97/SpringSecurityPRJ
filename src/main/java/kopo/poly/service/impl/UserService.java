package kopo.poly.service.impl;

import kopo.poly.auth.AuthInfo;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.UserInfoRepository;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.IUserService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserInfoRepository userInfoRepository;

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getUserIdExists Start!!!");
        AtomicReference<UserInfoDTO> atomicReference = new AtomicReference<>();

        userInfoRepository.findByUserId(pDTO.userId()).ifPresentOrElse(entity -> {
            atomicReference.set(UserInfoDTO.builder().existYn("Y").build());
        }, () -> {
            atomicReference.set(UserInfoDTO.builder().existYn("N").build());
        });
        log.info(this.getClass().getName() + ".getUserIdExists End!!!");
        return atomicReference.get();
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) {
        log.info(this.getClass().getName() + ".insertUserInfo Start!!!");
        int res = 0;

        log.info("pDTO : " + pDTO);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(pDTO.userId());

        if (rEntity.isPresent()) {
            res = 2;
        } else {
            UserInfoEntity pEntity = UserInfoDTO.of(pDTO);
            userInfoRepository.save(pEntity);

            res = 1;
        }
        log.info(this.getClass().getName() + ".insertUserInfo End!!!");

        return res;
    }

    @Override
    public UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getUserInfo Start!!!");
        String userId = CmmUtil.nvl(pDTO.userId());
        log.info("userId : " + userId);

        UserInfoDTO rDTO = UserInfoDTO.from(userInfoRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException(userId)));

        log.info(this.getClass().getName() + ".getUserInfo End!!!");

        return rDTO;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(this.getClass().getName() + ".loadUserByUsername Start!!!!!");
        log.info("userId : " + username);

        UserInfoEntity rEntity = userInfoRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " Not Found User"));

        UserInfoDTO rDTO = UserInfoDTO.from(rEntity);
        log.info(this.getClass().getName() + ".loadUserByUsername End!!!!!");

        return new AuthInfo(rDTO);
    }
}
