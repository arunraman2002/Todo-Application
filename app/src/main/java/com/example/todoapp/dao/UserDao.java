package com.example.todoapp.dao;

import com.example.todoapp.model.UserProfile;

public interface UserDao {

    long insert(final UserProfile userProfile);
    UserProfile getUserProfile();
    long update(final UserProfile userProfile);
    void open();
    void close();
}
