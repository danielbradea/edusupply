package com.bid90.edusupply.controller;


import com.bid90.edusupply.model.User;
import com.bid90.edusupply.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {



    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    List<User> getAllUser(){
        return userService.getAllUser();
    }

    @PostMapping("")
    User createUser(User user){
        return userService.addUser(user);
    }


}
