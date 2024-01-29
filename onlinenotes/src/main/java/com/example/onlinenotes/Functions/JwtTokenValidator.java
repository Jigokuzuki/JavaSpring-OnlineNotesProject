package com.example.onlinenotes.Functions;

import io.jsonwebtoken.Jwts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class JwtTokenValidator {

    private String secretkey = "CBNSJKgapihgnaiopsujxJ29N9FUJ39JAOF";

    public ResponseEntity<?> validateToken(Map<String, String> headers) {
        String token = headers.get("authorization");

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final String jwString = token.split(" ")[1].trim();
        try {
            SecretKey secretKey = new SecretKeySpec(secretkey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwString);
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return null;
    }
}
