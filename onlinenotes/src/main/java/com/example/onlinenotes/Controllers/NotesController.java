package com.example.onlinenotes.Controllers;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.onlinenotes.Dtos.CreateNoteDto;
import com.example.onlinenotes.Dtos.NoteDto;
import com.example.onlinenotes.Dtos.UpdateNoteDto;
import com.example.onlinenotes.Entities.EntityExtensions;
import com.example.onlinenotes.Entities.Note;
import com.example.onlinenotes.Functions.JwtTokenValidator;
import com.example.onlinenotes.Repositories.INotesRepository;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private INotesRepository notesRepository;

    @Autowired
    private JwtTokenValidator tokenValidator;

    @CrossOrigin(origins = "http://localhost:5238")
    @GetMapping
    public List<NoteDto> getAllNotes() {
        return notesRepository.getAll().stream()
                .map(EntityExtensions::AsDto)
                .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "http://localhost:5238")
    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable int id) {
        Note note = notesRepository.getById(id);

        if (note == null) {
            System.out.println(note);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityExtensions.AsDto(note));
    }

    @CrossOrigin(origins = "http://localhost:5238")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteDto>> getUserNotes(@PathVariable int userId) {
        List<NoteDto> userNotes = notesRepository.getNotesByUserId(userId)
                .stream()
                .map(EntityExtensions::AsDto)
                .collect(Collectors.toList());

        if (userNotes == null || userNotes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userNotes);
    }

    @CrossOrigin(origins = "http://localhost:5238")
    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody CreateNoteDto noteDto,
            @RequestHeader Map<String, String> headers) {

        ResponseEntity<?> tokenResponse = tokenValidator.validateToken(headers);
        if (tokenResponse != null) {
            return tokenResponse;
        }

        try {

            if (StringUtils.isEmpty(noteDto.Title())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Title cannot be empty or longer than 10 characters!"));
            }

            if (StringUtils.isEmpty(noteDto.Content())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Content cannot be empty!"));
            }

            if (StringUtils.isEmpty(noteDto.Category())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Category cannot be empty or longer than 10 characters!"));
            }

            Note note = new Note();
            note.setTitle(noteDto.Title());
            note.setContent(noteDto.Content());
            note.setCreatedDate(OffsetDateTime.now());
            note.setModifiedDate(OffsetDateTime.now());
            note.setCategory(noteDto.Category());
            note.setIsFavorite(noteDto.IsFavorite());
            note.setColor(noteDto.Color());

            note.setCreatedDate(note.getCreatedDate().minusNanos(note.getCreatedDate().getNano()));
            note.setModifiedDate(note.getModifiedDate().minusNanos(note.getModifiedDate().getNano()));
            notesRepository.create(note);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(note.getId())
                    .toUri();

            return ResponseEntity.created(location).body(EntityExtensions.AsDto(note));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @CrossOrigin(origins = "http://localhost:5238")
    @PutMapping("/{id}")
    public ResponseEntity<?> editNote(@PathVariable int id, @RequestBody UpdateNoteDto updateNoteDto,
            @RequestHeader Map<String, String> headers) {

        ResponseEntity<?> tokenResponse = tokenValidator.validateToken(headers);
        if (tokenResponse != null) {
            return tokenResponse;
        }

        try {
            Note existingNote = notesRepository.getById(id);

            if (existingNote == null) {
                return ResponseEntity.notFound().build();
            }

            if (StringUtils.isEmpty(updateNoteDto.Title()) || updateNoteDto.Title().length() > 10) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Title cannot be empty or longer than 10 characters!"));
            }

            if (StringUtils.isEmpty(updateNoteDto.Content())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Content cannot be empty!"));
            }

            if (StringUtils.isEmpty(updateNoteDto.Category()) || updateNoteDto.Category().length() > 20) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Category cannot be empty or longer than 20 characters!"));
            }

            existingNote.setTitle(updateNoteDto.Title());
            existingNote.setContent(updateNoteDto.Content());
            existingNote.setCategory(updateNoteDto.Category());
            existingNote.setModifiedDate(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            existingNote.setIsFavorite(updateNoteDto.IsFavorite());
            existingNote.setColor(updateNoteDto.Color());

            notesRepository.update(existingNote);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @CrossOrigin(origins = "http://localhost:5238")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable int id,
            @RequestHeader Map<String, String> headers) {

        ResponseEntity<?> tokenResponse = tokenValidator.validateToken(headers);
        if (tokenResponse != null) {
            return tokenResponse;
        }

        try {
            Note note = notesRepository.getById(id);

            if (note != null) {
                notesRepository.delete(id);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

}
