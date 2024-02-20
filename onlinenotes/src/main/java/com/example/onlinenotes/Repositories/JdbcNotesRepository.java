package com.example.onlinenotes.Repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.onlinenotes.Entities.Note;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Repository
@Transactional
public class JdbcNotesRepository implements INotesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Note> getAll() {
        return entityManager.createQuery("SELECT n FROM Note n", Note.class)
                .getResultList();
    }

    @Override
    public Note getById(int id) {
        try {
            return entityManager.createQuery("SELECT n from Note n WHERE n.id = :id", Note.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void create(Note note) {
        entityManager.persist(note);
    }

    @Override
    public void update(Note updatedNote) {
        entityManager.createQuery(
                "UPDATE Note n SET n.Title = :title, n.Content = :content, n.Category = :category, n.IsFavorite = :isfavorite WHERE n.Id = :id")
                .setParameter("title", updatedNote.getTitle())
                .setParameter("content", updatedNote.getContent())
                .setParameter("category", updatedNote.getCategory())
                .setParameter("isfavorite", updatedNote.getIsFavorite())
                .setParameter("id", updatedNote.getId())
                .executeUpdate();
    }

    @Override
    public void delete(int id) {
        entityManager.createQuery("DELETE FROM Note n WHERE n.Id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<Note> getNotesByUserId(int userId) {
        List<Integer> userNoteIds = entityManager.createQuery(
                "SELECT un.NoteId FROM UserNotes un WHERE un.UserId = :userId", Integer.class)
                .setParameter("userId", userId)
                .getResultList();

        return entityManager.createQuery("SELECT n FROM Note n WHERE n.Id IN :noteIds", Note.class)
                .setParameter("noteIds", userNoteIds)
                .getResultList();
    }
}
