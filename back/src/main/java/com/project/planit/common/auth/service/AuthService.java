package com.project.planit.common.auth.service;

import com.project.planit.common.auth.jwt.JwtProvider;
import com.project.planit.common.auth.userDetails.PrincipalDetails;
import com.project.planit.member.entity.Member;
import com.project.planit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.project.planit.common.auth.service fileName       : AuthService author
 *   : SSAFY date           : 2023-02-09 description    :
 * =========================================================== DATE              AUTHOR
 * NOTE ----------------------------------------------------------- 2023-02-09        SSAFY       최초
 * 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
  private final MemberRepository memberRepository;
  private final JwtProvider jwtProvider;


  public String reissueAccessToken(String oldAccessToken, String refreshToken) {
    if (!jwtProvider.validateToken(refreshToken)) {
      throw new RuntimeException("invalid refresh token");
    }

    Authentication authentication = jwtProvider.getAuthentication(oldAccessToken);
    Member member = ((PrincipalDetails) authentication.getPrincipal()).getMember();

    log.info("access token reissue 대상: {}", member);

    Member findMember = memberRepository.findByAppId(member.getAppId())
        .orElseThrow(() -> new RuntimeException("Not found user"));

    if (!refreshToken.equals(findMember.getRefreshToken())) {
      throw new RuntimeException("invalid refresh token");
    }

    return jwtProvider.createAccessToken(authentication, findMember.getId(), findMember.getName());
  }
}
