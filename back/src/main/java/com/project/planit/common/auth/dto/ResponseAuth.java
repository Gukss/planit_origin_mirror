package com.project.planit.common.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.project.planit.common.auth.dto fileName       : ResponseAuth author
 * : SSAFY date           : 2023-02-10 description    :
 * =========================================================== DATE              AUTHOR
 * NOTE ----------------------------------------------------------- 2023-02-10        SSAFY       최초
 * 생성
 */
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ResponseAuth {
  private String accessToken;
  private String refreshToken;
}
