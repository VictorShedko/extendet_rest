package com.epam.esm.gift_extended.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTProvider {

    private static final Long ONE_MINUTE = 60_000L;


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.duration}")
    private Integer JWTDuration;


    public String generateToken(String login) {
        Date date = new Date();
        long t = date.getTime();
        Date expirationTime = new Date(t + ONE_MINUTE * JWTDuration);

        return Jwts.builder()
                .setSubject(login)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
