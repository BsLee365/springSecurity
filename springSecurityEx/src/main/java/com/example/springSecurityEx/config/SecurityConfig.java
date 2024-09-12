package com.example.springSecurityEx.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 사용자 로그인시 비밀번호에 대해 단반향 해시 암호화를 진행하여 저장되어 있는 비밀번호와 대조한다.
    // 스프링 시큐리티는 암호화를 위해 BCrypt Password Encoder를 제공하고 권장하고 있다.
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // 암호화를 진행할 때 필요한 메서드
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // 상단에서부터 실행되기 때문에 순서를 잘 지켜줘야된다.
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated())
                        // 6.1부터 선언하는 방식 변경
                        .csrf((csrf) -> csrf.disable());

        // 접근되지 않은 페이지 접근시, /login으로 자동으로 이동.
        http.formLogin((auth) -> auth.loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .permitAll());





        return http.build();
    }
}
