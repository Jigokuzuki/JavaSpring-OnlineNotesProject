package com.example.onlinenotes.Controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.onlinenotes.Dtos.CreateUserDto;
import com.example.onlinenotes.Dtos.LoginUserDto;
import com.example.onlinenotes.Dtos.UserDto;
import com.example.onlinenotes.Entities.EntityExtensions;
import com.example.onlinenotes.Entities.User;
import com.example.onlinenotes.Repositories.IUsersRepositories;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/users")
public class UsersContoller {

    @Autowired
    private IUsersRepositories usersRepository;

    @GetMapping
    public List<UserDto> getAllNotes() {
        return usersRepository.getAll().stream()
                .map(EntityExtensions::AsDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getNoteById(@PathVariable int id) {
        User user = usersRepository.getById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityExtensions.AsDto(user));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto userDto) {
        try {
            User existingUser = usersRepository.getUserByEmail(userDto.Email());

            if (existingUser != null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "User already exists!"));
            }

            if (StringUtils.isEmpty(userDto.FirstName())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "First name cannot be empty!"));
            }

            if (StringUtils.isEmpty(userDto.Surname())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Surname cannot be empty!"));
            }

            if (StringUtils.isEmpty(userDto.Email())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email cannot be empty!"));
            }

            if (StringUtils.isEmpty(userDto.Password()) || userDto.Password().length() < 8
                    || !containsSpecialCharacter(userDto.Password())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid password!"));
            }

            User user = new User();
            user.setFirstName(userDto.FirstName());
            user.setSurname(userDto.Surname());
            user.setEmail(userDto.Email());
            user.setPassword(userDto.Password());
            usersRepository.create(user);

            String token = generateToken(user);

            return ResponseEntity.ok(Map.of("id", user.getId(), "token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            User user = usersRepository.getById(id);

            if (user != null) {
                usersRepository.delete(id);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserDto userDto) {
        try {
            User user = usersRepository.getUserByEmail(userDto.Email());

            if (user == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "User does not exist!"));
            }

            if (StringUtils.isEmpty(userDto.Email())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email cannot be empty!"));
            }

            if (StringUtils.isEmpty(userDto.Password()) || userDto.Password().length() < 8
                    || !containsSpecialCharacter(userDto.Password())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid password!"));
            }

            String token = generateToken(user);

            return ResponseEntity.ok(Map.of("id", user.getId(), "token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    private boolean containsSpecialCharacter(String password) {
        String specialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/";
        for (char c : specialChars.toCharArray()) {
            if (password.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    private String generateToken(User user) {
        String secretKey = "CBNSJKgapihgnaiopsujxJ29N9FUJ39JAOF";
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600000);

        byte[] keyBytes = secretKey.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(Integer.toString(user.getId()))
                .claim("aud",
                        List.of("http://localhost:6044", "https://localhost:44391", "http://localhost:5046",
                                "https://localhost:7119"))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
