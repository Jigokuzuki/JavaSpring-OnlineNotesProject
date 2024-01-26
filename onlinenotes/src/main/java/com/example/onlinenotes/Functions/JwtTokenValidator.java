package com.example.onlinenotes.Functions;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class JwtTokenValidator {

    private String secretkey = "CBNSJKgapihgnaiopsujxJ29N9FUJ39JAOF";

    public record ValidationResult(boolean isValid, String errorMessage) {
    }

    public ValidationResult validateToken(Map<String, String> headers) {
        String token = headers.get("authorization");
        if (token == null || token.isEmpty()) {
            return new ValidationResult(false, "Token is missing");
        }
        final String jwString = token.split(" ")[1].trim();
        try {
            SecretKey secretKey = new SecretKeySpec(secretkey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwString);
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return new ValidationResult(false, "Invalid token");
        }

        return new ValidationResult(true, null);
    }

    public String getIdFromToken(Map<String, String> headers) {
        String token = headers.get("authorization");
        if (token == null || token.isEmpty()) {
            return "";
        }

        // Remove the "Bearer " prefix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            SecretKey secretKey = new SecretKeySpec(secretkey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("id", String.class);
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return "";
        }
    }
}
