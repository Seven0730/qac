package com.team12.user.config;

import com.team12.user.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String secretKey = "9eJ8grwCm6h2bXY7bCZzvQU9GRjVQWI9fXbXSY8/j8U=";
    private final long validityInMilliseconds = 3600000; // 1 hour

    public String createToken(String username, Role role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        // TODO: FIX
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while generating JWT", e);
        }
    }

    // TODO: FIX
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
