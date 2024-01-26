package com.example.onlinenotes.Controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.onlinenotes.Dtos.CreateUserDto;
import com.example.onlinenotes.Dtos.LoginUserDto;
import com.example.onlinenotes.Dtos.UserDto;
import com.example.onlinenotes.Entities.EntityExtensions;
import com.example.onlinenotes.Entities.User;
import com.example.onlinenotes.Functions.*;
import com.example.onlinenotes.Repositories.IUsersRepository;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/users")
public class UsersContoller {

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    JwtTokenGenerator tokenGenerator;

    @Autowired
    SpecialCharacter specialCharacter;

    @Autowired
    JwtTokenValidator tokenValidator;

    @CrossOrigin(origins = "http://localhost:5238")
    @GetMapping
    public List<UserDto> getAllNotes() {
        return usersRepository.getAll().stream()
                .map(EntityExtensions::AsDto)
                .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "http://localhost:5238")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getNoteById(@PathVariable int id) {
        User user = usersRepository.getById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityExtensions.AsDto(user));
    }

    @CrossOrigin(origins = "http://localhost:5238")
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
                    || !specialCharacter.containsSpecialCharacter(userDto.Password())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid password!"));
            }

            User user = new User();
            user.setFirstName(userDto.FirstName());
            user.setSurname(userDto.Surname());
            user.setEmail(userDto.Email());
            user.setPassword(userDto.Password());
            usersRepository.create(user);

            String token = tokenGenerator.generateToken(user);

            return ResponseEntity.ok(Map.of("id", user.getId(), "token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @CrossOrigin(origins = "http://localhost:5238")
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

    @CrossOrigin(origins = "http://localhost:5238")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserDto userDto) {
        try {

            User user = usersRepository.getUserByEmail(userDto.Email());

            if (user == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "User not found"));
            }

            if (StringUtils.isEmpty(userDto.Email())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email cannot be empty!"));
            }

            if (StringUtils.isEmpty(userDto.Password()) || userDto.Password().length() < 8
                    || !specialCharacter.containsSpecialCharacter(userDto.Password())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid password!"));
            }

            String token = tokenGenerator.generateToken(user);

            return ResponseEntity.ok(Map.of("id", user.getId(), "token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

}
