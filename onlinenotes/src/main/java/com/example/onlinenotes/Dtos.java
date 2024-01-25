package com.example.onlinenotes;

import java.time.OffsetDateTime;

import jakarta.annotation.Nonnull;

public class Dtos {

        public record NoteDto(
                        int Id,
                        String Title,
                        String Content,
                        OffsetDateTime CreatedDate,
                        OffsetDateTime ModifiedDate,
                        String Category,
                        boolean IsFavorite,
                        String Color) {

        }

        public record CreateNoteDto(
                        @Nonnull String Title,
                        @Nonnull String Content,
                        OffsetDateTime CreatedDate,
                        OffsetDateTime ModifiedDate,
                        @Nonnull String Category,
                        boolean IsFavorite,
                        @Nonnull String Color) {

        }

        public record UpdateNoteDto(
                        @Nonnull String Title,
                        @Nonnull String Content,
                        OffsetDateTime CreatedDate,
                        OffsetDateTime ModifiedDate,
                        @Nonnull String Category,
                        boolean IsFavorite,
                        @Nonnull String Color) {

        }

        public record UserDto(
                        int Id,
                        String FirstName,
                        String Surname,
                        String Email,
                        String Password) {
        }

        public record LoginUserDto(
                        @Nonnull String Email,
                        @Nonnull String Password) {

        }

        public record CreateUserDto(
                        @Nonnull String FirstName,
                        @Nonnull String Surname,
                        @Nonnull String Email,
                        @Nonnull String Password) {

        }

        public record UpdateUserDto(
                        @Nonnull String FirstName,
                        @Nonnull String Surname,
                        @Nonnull String Email,
                        @Nonnull String Password) {

        }

        public record UserNotesDto(
                        int Id,
                        int UserId,
                        int NoteId) {
        }

        public record CreateUserNotesDto(
                        int UserId,
                        int NoteId) {
        }

        public record UpdateUserNotesDto(
                        int UserId,
                        int NoteId) {
        }

}
