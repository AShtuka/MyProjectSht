package ua.com.osht.myproject.service;

import ua.com.osht.myproject.domain.User;

public interface UserService {
    boolean addUser(User user);
    boolean activateUser(String code);
}

