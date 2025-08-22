package com.bid90.edusupply.service;

import com.bid90.edusupply.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    List<User> getAllUser();

    User addUser(User user);
}
