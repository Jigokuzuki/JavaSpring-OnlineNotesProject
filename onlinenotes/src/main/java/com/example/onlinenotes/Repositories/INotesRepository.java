package com.example.onlinenotes.Repositories;

import java.util.List;
import com.example.onlinenotes.Entities.Note;

public interface INotesRepository {

    List<Note> getAll();

    Note getById(int id);

    void create(Note note);

    void update(Note updatedNote);

    void delete(int id);

    List<Note> getNotesByUserId(int userId);

}