package com.project.planit.common.auth.config;

import com.project.planit.common.auth.jwt.JwtAccessDeniedHandler;
import com.project.planit.common.auth.jwt.JwtAccessSuccessHandler;
import com.project.planit.common.auth.jwt.JwtAuthenticationEntryPoint;
import com.project.planit.common.auth.jwt.JwtAuthenticationFilter;
import com.project.planit.common.auth.jwt.JwtProvider;
import com.project.planit.common.auth.userDetails.PrincipalDetails;
import com.project.planit.common.auth.userDetails.PrincipalDetailsService;
import com.project.planit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * packageName    : com.project.planit.common.auth.config fileName       : SecurityConfig author
 *     : SSAFY date           : 2023-02-09 description    :
 * =========================================================== DATE              AUTHOR
 * NOTE ----------------------------------------------------------- 2023-02-09        SSAFY       최초
 * 생성
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final MemberRepository memberRepository;
  private final JwtProvider jwtProvider;
  private final PrincipalDetailsService principalDetailsService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new JwtAccessSuccessHandler(memberRepository, jwtProvider);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.authorizeHttpRequests()
        // POST /users는 인증이 되어야 접근 가능
        .antMatchers( "/api/stoarges").authenticated()
        .antMatchers( "/api/votes").authenticated()
        .antMatchers( "/api/rooms").authenticated()
        .antMatchers( "/api/rooms").authenticated()
        .antMatchers( "/api/rooms/users").authenticated()
        .antMatchers( "/api/chatting").authenticated()
        .antMatchers( "/api/notification").authenticated()
        .antMatchers( "/api/votes/vote-item").authenticated()
        .antMatchers( "/api/votes/vote-item/user").authenticated()
        // 그외 모든 요청은 허용
        .anyRequest().permitAll()
        .and()
        .logout()
        .logoutSuccessUrl("/");
//        .and()
//        .oauth2Login()
//        .successHandler(authenticationSuccessHandler())
//        .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정을 저장
//        .userService(principalDetailsService); // OAuth2 로그인 성공 시, 후작업을 진행할 UserService 인터페이스 구현체 등록

    // jwt 사용을 위해 session 해제
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
//        .addFilter(corsFilter)
        .formLogin().disable() //form login 안함
        .httpBasic().disable();

    // jwt 필터 추가
    //addFilterBefore(new JwtFilterConfig(jwtService), ...)
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    // jwt 인증 실패시 exception handler 등록
    http.exceptionHandling()
        .accessDeniedHandler(jwtAccessDeniedHandler)
        .authenticationEntryPoint(jwtAuthenticationEntryPoint);

    return http.build();
  }

}
