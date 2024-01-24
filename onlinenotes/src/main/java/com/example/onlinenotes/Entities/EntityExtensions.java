package com.example.onlinenotes.Entities;

import com.example.onlinenotes.Dtos.NoteDto;
import com.example.onlinenotes.Dtos.UserDto;
import com.example.onlinenotes.Dtos.UserNotesDto;

public class EntityExtensions {
    public static NoteDto AsDto(Note note) {
        return new NoteDto(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedDate(),
                note.getModifiedDate(),
                note.getCategory(),
                note.getIsFavorite(),
                note.getColor());
    }

    public static UserDto AsDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword());
    }

    public static UserNotesDto AsDto(UserNotes userNotes) {
        return new UserNotesDto(
                userNotes.getId(),
                userNotes.getUserId(),
                userNotes.getNoteId());
    }
}
