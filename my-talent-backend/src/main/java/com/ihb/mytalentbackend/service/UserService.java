package com.ihb.mytalentbackend.service;

import com.ihb.mytalentbackend.domain.Role;
import com.ihb.mytalentbackend.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findUserByUsername(String username);
    void changeRole(String username, Role newRole);
    void deleteUser(Long id);
    List<User> findAllUsers();
}

