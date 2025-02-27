package org.example.letter.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.letter.domain.auth.auth.token.AuthTokens;
import org.example.letter.domain.auth.auth.token.AuthTokensGenerator;
import org.example.letter.domain.auth.auth.token.JwtTokenProvider;
import org.example.letter.domain.auth.dto.LoginResponse;
import org.example.letter.domain.user.entity.LoginType;
import org.example.letter.domain.user.entity.User;
import org.example.letter.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthTokensGenerator authTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${kakao.key.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    /** Web 버전 카카오 로그인 **/
    public LoginResponse kakaoLogin(String code) { //, currentDomain)
        // 0. 동적으로 redirect URI 선택
        //String redirectUri = selectRedirectUri(currentDomain);

        // 1. 인가 코드로 액세스 토큰 요청 --> getAccessToken(code, redirectUri)에서 위 과정 생략으로 바꿈.
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        HashMap<String, Object> userInfo = getKakaoUserInfo(accessToken);

        // 3. 카카오 ID로 회원가입 && 로그인 처리
        LoginResponse kakaoUserResponse = kakaoUserLogin(userInfo);

        return kakaoUserResponse;
    }

    //1. "인가 코드"로 "액세스 토큰" 요청
    private String getAccessToken(String code) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode.get("access_token").asText(); //토큰 전송
    }

    //2. 토큰으로 카카오 API 호출
    private HashMap<String, Object> getKakaoUserInfo(String accessToken) {
        HashMap<String, Object> userInfo= new HashMap<String,Object>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        userInfo.put("id",id);
        userInfo.put("email",email);
        userInfo.put("nickname",nickname);

        return userInfo;
    }

    //3. 카카오ID로 회원가입 & 로그인 처리
    private LoginResponse kakaoUserLogin(HashMap<String, Object> userInfo){

        Long uid= Long.valueOf(userInfo.get("id").toString());
        String kakaoEmail = userInfo.get("email").toString();
        String nickName = userInfo.get("nickname").toString();

        User kakaoUser = userRepository.findByEmail(kakaoEmail).orElse(null);

        if (kakaoUser == null) {    //회원가입
            kakaoUser= new User();
            kakaoUser.setUid(UUID.randomUUID());
            kakaoUser.setNickname(nickName);
            kakaoUser.setEmail(kakaoEmail);
            kakaoUser.setLoginType(LoginType.valueOf("kakao".toUpperCase()));
            userRepository.save(kakaoUser);
        }
        //토큰 생성
        AuthTokens token=authTokensGenerator.generate(uid.toString());
        return new LoginResponse(uid,nickName,kakaoEmail,token);
    }

}
