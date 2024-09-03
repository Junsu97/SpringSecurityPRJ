package kopo.poly.auth;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.util.CmmUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
@RequiredArgsConstructor
public class AuthInfo implements UserDetails {
    private final UserInfoDTO userInfoDTO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return CmmUtil.nvl(userInfoDTO.password());
    }

    @Override
    public String getUsername() {
        return CmmUtil.nvl(userInfoDTO.userId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
