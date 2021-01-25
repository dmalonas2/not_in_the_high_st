package com.example.demo.service;

import com.example.demo.model.persistence.User;

public interface UserService {
    User createUser(String username, String password);
}
