package com.example.onlinenotes.Repositories;

import java.util.List;

import com.example.onlinenotes.Entities.UserNotes;

public interface IUserNotesRepository {

    List<UserNotes> getAll();

    UserNotes getById(int id);

    void create(UserNotes usernotes);

    void update(UserNotes updatedUserNotes);

    void delete(int id);

    void deleteByNoteAndUser(int noteId, int userId);

    List<UserNotes> getByUserId(int userId);
}