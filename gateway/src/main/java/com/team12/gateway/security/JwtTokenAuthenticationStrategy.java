package com.team12.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public boolean authenticate(String token, String secretKey) {
        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String subject = claims.getSubject();
            return subject != null && !subject.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
