package org.dcode.artificialswbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                // JwtUtil의 validateAndGetUserId 메서드로 토큰 검증 및 userId 얻기
                Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));
                // 필요 시 가족 ID 등 추가 정보도 얻을 수 있음 Long familyId = jwtUtil.validateAndGetFamilyId(token);

                // 간단하게 ROLE_USER 권한 부여 (실제 역할은 토큰 클레임에 따라 할당 가능)
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

                // 인증 객체 생성 및 SecurityContext에 저장
                var authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // 토큰 검증 실패 시 SecurityContext 초기화 후 그대로 필터 체인 진행
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
