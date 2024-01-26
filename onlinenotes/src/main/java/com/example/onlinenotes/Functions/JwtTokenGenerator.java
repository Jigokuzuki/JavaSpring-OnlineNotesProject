package com.example.onlinenotes.Functions;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.example.onlinenotes.Entities.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenGenerator {
    public String generateToken(User user) {
        String secretKey = "CBNSJKgapihgnaiopsujxJ29N9FUJ39JAOF";
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600 * 1000); // Adding 3600 seconds in milliseconds

        byte[] keyBytes = secretKey.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .claim("unique_name", Integer.toString(user.getId()))
                .claim("aud", List.of(
                        "http://localhost:6044",
                        "https://localhost:44391",
                        "http://localhost:5046",
                        "https://localhost:7119"))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
