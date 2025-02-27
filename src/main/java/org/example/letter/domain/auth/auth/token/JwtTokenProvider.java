    package org.example.letter.domain.auth.auth.token;

    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.io.Decoders;
    import io.jsonwebtoken.security.Keys;
    import java.security.Key;
    import java.util.Date;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component
    public class JwtTokenProvider {

        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        private final Key key;

        public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey) {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }

        // 새로운 메서드: 이메일(subject)와 uid 클레임 포함하여 액세스 토큰 생성
        public String accessTokenGenerate(String email, String uid, Date expiredAt) {
            return Jwts.builder()
                    .setSubject(email)    // subject로 이메일 사용
                    .claim("uid", uid)   // 추가 클레임으로 uid 포함
                    .setExpiration(expiredAt)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }
        // refresh 토큰도 동일하게 생성 (subject: 이메일, 클레임: uid)
        public String refreshTokenGenerate(String email, String uid, Date expiredAt) {
            return Jwts.builder()
                    .setSubject(email)
                    .claim("uid", uid)
                    .setExpiration(expiredAt)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }
        // 토큰 유효성 검사
        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
                return false;
            }
        }

        // 토큰에서 subject(이메일)를 추출
        public String getSubject(String token) {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                    .getBody().getSubject();
        }

        // 토큰에서 특정 클레임 값을 추출 (예: uid)
        public String getClaim(String token, String claimName) {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                    .getBody().get(claimName, String.class);
        }
    }