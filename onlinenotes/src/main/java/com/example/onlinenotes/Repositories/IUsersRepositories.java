package com.example.onlinenotes.Repositories;

import java.util.List;

import com.example.onlinenotes.Entities.User;

public interface IUsersRepositories {

    List<User> getAll();

    User getById(int id);

    void create(User user);

    void update(User updatedUser);

    void delete(int id);

    User getUserByEmail(String email);

}