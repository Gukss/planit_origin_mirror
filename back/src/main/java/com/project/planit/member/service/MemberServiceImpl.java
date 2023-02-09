package com.project.planit.member.service;

import com.project.planit.common.auth.jwt.JwtProvider;
import com.project.planit.common.auth.userDetails.PrincipalDetails;
import com.project.planit.common.exception.NotFoundExceptionMessage;
import com.project.planit.common.exception.NotFoundMemberException;
import com.project.planit.member.dto.CreateMemberRequest;
import com.project.planit.member.dto.ReadMemberResponse;
import com.project.planit.member.dto.SignInMemberResponse;
import com.project.planit.member.dto.UpdateMemberRequest;

import com.project.planit.member.entity.Member;
import com.project.planit.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.project.planit.member.service fileName       : MemberService author
 * : SSAFY date           : 2023-01-26 description    :
 * =========================================================== DATE              AUTHOR
 * NOTE ----------------------------------------------------------- 2023-01-26        SSAFY       최초
 * 생성
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;
  private final JwtProvider jwtProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  @Transactional
  @Override
  public Long createMember(CreateMemberRequest request) {
    // @TODO : 토큰으로 던져줘야함
    Member member=Member.create(request);
    Member newMember=memberRepository.save(member);
    return newMember.getId();
  }

  @Transactional
  @Override
  public void updateMember(Long id, UpdateMemberRequest request) {
    Member member= memberRepository.findById(id)
        .orElseThrow(() -> new NotFoundMemberException(NotFoundExceptionMessage.USER_NOT_FOUND));

    member.update(request);
  }

  @Override
  public boolean existMemberId(String memberAppId) {
    if(memberRepository.findByAppId(memberAppId).isPresent()){
      // 멤버 아이디가 중복이면 false반환
      return false;
    }
    return true;
  }

  @Override
  public ReadMemberResponse readMember(Long memberId) {
    Member member=memberRepository.findById(memberId)
        .orElseThrow(() -> new NotFoundMemberException(NotFoundExceptionMessage.USER_NOT_FOUND));

    ReadMemberResponse response=ReadMemberResponse.builder()
        .memberAppId(member.getAppId())
        .memberName(member.getName())
        .memberEmail(member.getEmail())
        .build();

    return response;
  }

  @Override
  public List<ReadMemberResponse> readMemberListByMemberId(String memberAppId) {
    List<Member> memberList=memberRepository.findAllByAppId(memberAppId)
        .orElseThrow(() -> new NotFoundMemberException(NotFoundExceptionMessage.USER_LIST_NOT_FOUND));

    List<ReadMemberResponse> response=new ArrayList<>();

    for(Member member:memberList){
      ReadMemberResponse readMemberResponse=ReadMemberResponse.builder()
          .memberAppId(member.getAppId())
          .memberName(member.getName())
          .memberEmail(member.getEmail())
          .build();

      response.add(readMemberResponse);
    }

    return response;
  }

  @Override
  public String findMemberIdByMemberEmail(String email) {
    Member member=memberRepository.findByEmail(email)
        .orElseThrow(()->new NotFoundMemberException(NotFoundExceptionMessage.USER_NOT_FOUND));
    return member.getAppId();
  }

  @Override
  public String findMemberPwdByMemberIdAndMemberEmail(String memberAppId,String email) {
    Member member= memberRepository.findByAppIdAndEmail(memberAppId,email)
        .orElseThrow(()->new NotFoundExceptionMessage(NotFoundExceptionMessage.USER_EMAIL_FIND_NOT_FOUND));
    return member.getAppPwd();
  }

  public Member findMemberByMemberAppId(String memberAppId){
    Member member = memberRepository.findByAppId(memberAppId).orElseThrow(()->new NotFoundMemberException(NotFoundExceptionMessage.USER_NOT_FOUND));
    return member;
  }

  // username 과 패스워드로 사용자를 인증하여 액세스토큰을 반환한다.
  public SignInMemberResponse createAccessToken(String memberAppId, String memberAppPwd) {
    // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(memberAppId, memberAppPwd);

    // authenticationToken 객체를 통해 Authentication 객체 생성
    // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    // 그 객체를 시큐리티 컨텍스트에 저장
    SecurityContextHolder.getContext().setAuthentication(authentication);
    PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
    Member member = principal.getMember();
    // 인증 정보를 기준으로 jwt access 토큰 생성
    String accessToken = jwtProvider.createAccessToken(authentication, member.getId(), member.getName());

    return SignInMemberResponse.builder()
        .accessToken(accessToken)
        .build();
  }

  public SignInMemberResponse createRefreshToken(String memberAppId, String memberAppPwd) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(memberAppId, memberAppPwd);
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    String refreshToken = jwtProvider.createRefreshToken(authentication);



    return SignInMemberResponse.builder()
        .accessToken(refreshToken)
        .build();
  }
}
