package com.bid90.edusupply.controller;


import com.bid90.edusupply.dto.user.RegisterUserDTO;
import com.bid90.edusupply.dto.user.UpdateUserDTO;
import com.bid90.edusupply.dto.user.UserDTO;
import com.bid90.edusupply.model.User;
import com.bid90.edusupply.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {



    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list of all registered users.
     *
     * @return a list containing all {@link UserDTO} entities
     */
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    @GetMapping("")
    public List<UserDTO> getAllUser() {
        return userService.getAllUser().stream().map(UserDTO::new).toList();
    }

    /**
     * Creates and registers a new user.
     *
     * @param user DTO containing the user registration details
     * @return the created {@link UserDTO} entity
     */
    @Operation(summary = "Add new user", description = "Registers a new user in the system.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("")
    public ResponseEntity<UserDTO> addNewUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration details", required = true)
            @RequestBody RegisterUserDTO user) {


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserDTO(userService.addNewUser(user)));
    }

    @Operation(summary = "Update user", description = "Partially updates user data by ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> patchUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @RequestBody UpdateUserDTO updateUserDTO) {

        User updatedUser = userService.updateUser(id, updateUserDTO);
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     * @return 204 No Content if the deletion was successful
     */
    @Operation(summary = "Delete user", description = "Deletes a user by ID.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }





}
