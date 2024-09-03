package kopo.poly.config;

import kopo.poly.auth.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info(this.getClass().getName() + ".passwordEncoder Start!!!");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info(this.getClass().getName() + ".filterChain Start!!!");
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/notice/v1/**").hasAnyAuthority(UserRole.USER.getRole())
                        .requestMatchers("/user/v1/**").authenticated()
                        .requestMatchers("/html/user/**").authenticated()

                        .requestMatchers("/admin/**").hasAnyAuthority(UserRole.ADMIN.getRole())
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/html/ss/login.html")
                        .loginProcessingUrl("/login/v1/loginProc")
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        .successForwardUrl("/login/v1/loginSuccess")
                        .failureForwardUrl("/login/v1/loginFail")
                )
                .logout(logout -> logout
                        .logoutUrl("/user/v1/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/html/index.html")
                );
        return http.build();
    }
}
