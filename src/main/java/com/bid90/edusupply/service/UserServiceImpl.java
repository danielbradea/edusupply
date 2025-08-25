package com.bid90.edusupply.service;

import com.bid90.edusupply.dto.user.RegisterUserDTO;
import com.bid90.edusupply.dto.user.UpdateUserDTO;
import com.bid90.edusupply.exception.UserException;
import com.bid90.edusupply.model.User;
import com.bid90.edusupply.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User addNewUser(RegisterUserDTO user) {
        checkEmail(user.getEmail());
        checkName(user.getName());
        checkPassword(user.getPassword());
        var newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setRole(user.getRole());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEnabled(user.getEnabled());
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserException("User with id " + id + " not found",
                HttpStatus.NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public User updateUser(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User with id " + id + " not found", HttpStatus.NOT_FOUND));


        Optional.of(dto.getEmail()).ifPresent(email -> {
            checkEmail(email);
            user.setEmail(email);
        });


        Optional.of(dto.getName()).ifPresent(name -> {
            checkName(name);
            user.setName(name);
        });


        Optional.of(dto.getPassword()).ifPresent(pass -> {
            checkPassword(pass);
            user.setPassword(passwordEncoder.encode(pass));
        });

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long aLong) {
        return userRepository.getUserById(aLong);
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    void checkEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new UserException("Invalid email format", HttpStatus.BAD_REQUEST);
        }
    }

    void checkName(String name) {
        if (name.trim().isEmpty()) {
            throw new UserException("Name cannot be empty", HttpStatus.BAD_REQUEST);
        }
    }

    void checkPassword(String pass) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!Pattern.matches(passwordPattern, pass)) {
            throw new UserException(
                    "Password must be at least 8 characters, include an uppercase letter, a number, and a special character",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserException("User not found with email: " + username,HttpStatus.NOT_FOUND));
    }



}
