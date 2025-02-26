package org.example.letter.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.example.letter.domain.auth.auth.token.AuthTokens;
import org.example.letter.domain.auth.auth.token.AuthTokensGenerator;
import org.example.letter.domain.auth.dto.LoginResponse;
import org.example.letter.domain.auth.service.KakaoLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@SecurityScheme(name = "BearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "카카오톡 소셜 로그인 API", description = "카카오톡 소셜 로그인을 진행합니다.")
public class AuthController {

    private final KakaoLoginService kakaoLoginService;
    private final AuthTokensGenerator authTokensGenerator;

    @Operation(summary = "로그인 과정", description = "카카오톡 인가 코드를 파라미터로 이용하여 로그인을 진행합니다.")
    @Parameter(name = "code", description = "카카오톡 인가 코드")
    @ResponseBody
    @GetMapping("/login/oauth/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code){ //HttpServletRequest request){
        try {
            //현재 도메인 확인
            //String currentDomain = request.getServerName();
            return ResponseEntity.ok(kakaoLoginService.kakaoLogin(code));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found");
        }
    }
    @Operation(summary = "Access Token 재발급", security = @SecurityRequirement(name = "BearerAuth"), description = "헤더에 액세스 토큰을 담고 유효한 리프레쉬 토큰을 이용하여 새로운 액세스 토큰을 발급합니다.")
    @Parameter(name = "refreshToken", description = "리프레쉬 토큰 값")
    @ResponseBody
    @GetMapping("/token/refresh")
    public ResponseEntity<AuthTokens> refreshToken(@RequestParam String refreshToken) {
        try {
            AuthTokens tokens = authTokensGenerator.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token", e);
        }
    }
}
