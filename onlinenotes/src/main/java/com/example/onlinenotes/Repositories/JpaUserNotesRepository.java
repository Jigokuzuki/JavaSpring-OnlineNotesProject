package com.example.onlinenotes.Repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.onlinenotes.Entities.UserNotes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class JpaUserNotesRepository implements IUserNotesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserNotes> getAll() {
        return entityManager.createQuery("SELECT un FROM UserNotes un", UserNotes.class)
                .getResultList();
    }

    @Override
    public UserNotes getById(int id) {
        try {
            return entityManager.createQuery("SELECT un from UserNotes un WHERE un.Id = :id", UserNotes.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void create(UserNotes usernotes) {
        entityManager.persist(usernotes);
    }

    @Override
    public void update(UserNotes updatedUserNotes) {
        entityManager.createQuery(
                "UPDATE UserNotes un SET un.UserId = :userId, un.NoteId = :noteId WHERE un.Id = :id")
                .setParameter("userId", updatedUserNotes.getUserId())
                .setParameter("noteId", updatedUserNotes.getNoteId())
                .setParameter("id", updatedUserNotes.getId())
                .executeUpdate();
    }

    @Override
    public void delete(int id) {
        entityManager.createQuery("DELETE FROM UserNotes un WHERE un.Id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void deleteByNoteAndUser(int noteId, int userId) {
        entityManager.createQuery("DELETE FROM UserNotes un WHERE un.NoteId = :noteId AND un.UserId = :userId")
                .setParameter("noteId", noteId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public List<UserNotes> getByUserId(int userId) {
        return entityManager.createQuery("SELECT un FROM UserNotes un WHERE un.UserId = :userId", UserNotes.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
