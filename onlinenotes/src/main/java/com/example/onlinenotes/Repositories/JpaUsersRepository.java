package com.example.onlinenotes.Repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.onlinenotes.Entities.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class JpaUsersRepository implements IUsersRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    @Override
    public User getById(int id) {
        return entityManager.createQuery("SELECT u from User u WHERE u.Id = :id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void create(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User updatedUser) {
        entityManager.createQuery(
                "UPDATE uote u SET u.FirstName = :firstname, u.Surname = :surname, u.Email = :email, u.Password = :password WHERE u.Id = :id")
                .setParameter("id", updatedUser.getId())
                .setParameter("firstname", updatedUser.getFirstName())
                .setParameter("surname", updatedUser.getSurname())
                .setParameter("email", updatedUser.getEmail())
                .setParameter("password", updatedUser.getPassword())
                .executeUpdate();
    }

    @Override
    public void delete(int id) {
        entityManager.createQuery("DELETE FROM User u WHERE u.Id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public User getUserByEmail(String email) {
        List<User> resultList = entityManager.createQuery("SELECT u from User u WHERE u.Email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

        return resultList.isEmpty() ? null : resultList.get(0);
    }
}