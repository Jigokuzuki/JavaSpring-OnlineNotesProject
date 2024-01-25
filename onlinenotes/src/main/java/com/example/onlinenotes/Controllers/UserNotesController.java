package com.example.onlinenotes.Controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.onlinenotes.Dtos.CreateUserNotesDto;
import com.example.onlinenotes.Dtos.UpdateUserNotesDto;
import com.example.onlinenotes.Dtos.UserNotesDto;
import com.example.onlinenotes.Entities.EntityExtensions;
import com.example.onlinenotes.Entities.UserNotes;
import com.example.onlinenotes.Repositories.IUserNotesRepository;

@RestController
@RequestMapping("/usernotes")
public class UserNotesController {

    @Autowired
    private IUserNotesRepository usernotesRepository;

    @GetMapping
    public List<UserNotesDto> getAllNotes() {
        return usernotesRepository.getAll().stream()
                .map(EntityExtensions::AsDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserNotesDto> getNoteById(@PathVariable int id) {
        UserNotes usernotes = usernotesRepository.getById(id);

        if (usernotes == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityExtensions.AsDto(usernotes));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserNotesByUserId(@PathVariable int userId) {
        List<UserNotes> userNotes = usernotesRepository.getByUserId(userId);

        if (!userNotes.isEmpty()) {
            return ResponseEntity.ok(userNotes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User notes not found for userId: " + userId);
        }
    }

    @PostMapping
    public ResponseEntity<?> createUserNotes(@RequestBody CreateUserNotesDto userNotesDto) {
        try {
            UserNotes userNotes = new UserNotes();
            userNotes.setUserId(userNotesDto.UserId());
            userNotes.setNoteId(userNotesDto.NoteId());

            usernotesRepository.create(userNotes);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userNotes.getId())
                    .toUri();

            return ResponseEntity.created(location).body(userNotes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserNotes(@PathVariable int id, @RequestBody UpdateUserNotesDto updateUserNoteDto) {
        try {
            UserNotes existingUserNote = usernotesRepository.getById(id);

            if (existingUserNote == null) {
                return ResponseEntity.notFound().build();
            }

            existingUserNote.setUserId(updateUserNoteDto.UserId());
            existingUserNote.setNoteId(updateUserNoteDto.NoteId());

            usernotesRepository.update(existingUserNote);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserNotes(@PathVariable int id) {
        try {
            UserNotes userNotes = usernotesRepository.getById(id);

            if (userNotes != null) {
                usernotesRepository.delete(id);
            }

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/{noteId}")
    public ResponseEntity<?> deleteByNoteAndUser(@PathVariable int userId, @PathVariable int noteId) {
        try {
            usernotesRepository.deleteByNoteAndUser(noteId, userId);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

}
