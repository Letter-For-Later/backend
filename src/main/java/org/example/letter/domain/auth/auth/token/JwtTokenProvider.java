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

        public String accessTokenGenerate(String subject, Date expiredAt) {
            return Jwts.builder()
                    .setSubject(subject)    //uid
                    .setExpiration(expiredAt)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }

        public String refreshTokenGenerate(String subject, Date expiredAt) {
            return Jwts.builder()
                    .setSubject(subject)
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

        // 토큰에서 subject(여기서는 uid)를 추출
        public String getSubject(String token) {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                    .getBody().getSubject();
        }
    }