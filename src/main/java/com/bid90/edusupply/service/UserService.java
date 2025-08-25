package com.bid90.edusupply.service;

import com.bid90.edusupply.dto.user.RegisterUserDTO;
import com.bid90.edusupply.dto.user.UpdateUserDTO;
import com.bid90.edusupply.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;


public interface UserService {

    List<User> getAllUser();

    User addNewUser(RegisterUserDTO user);

    void deleteUser(Long id);

    User updateUser(Long id, UpdateUserDTO updateUserDTO);

    Optional<User> getUserById(Long aLong);

    UserDetails loadUserByUsername(String username);

    Optional<User> getUserByEmail(String email);
}
