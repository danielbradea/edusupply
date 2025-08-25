package com.bid90.edusupply.controller;

import com.bid90.edusupply.dto.group.CreateGroupDTO;
import com.bid90.edusupply.dto.group.GroupDTO;
import com.bid90.edusupply.dto.group.UpdateGroupDTO;
import com.bid90.edusupply.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing groups within the system.
 * Provides endpoints to create, update, list, and delete groups.
 */
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * Retrieves all groups in the system.
     *
     * @return list of {@link GroupDTO}
     */
    @Operation(summary = "Get all groups", description = "Retrieves a list of all groups available in the system.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "List of groups successfully retrieved")
    @GetMapping("")
    List<GroupDTO> getAllGroup() {
        return groupService.getAllGroup().stream().map(GroupDTO::new).toList();
    }

    /**
     * Creates a new group in the system.
     *
     * @param createGroupDTO data for the new group
     * @return the created group
     */
    @Operation(summary = "Create a group", description = "Creates a new group with the provided details.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("")
    ResponseEntity<GroupDTO> createGroup(@RequestBody CreateGroupDTO createGroupDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new GroupDTO(groupService.createGroup(createGroupDTO)));
    }

    /**
     * Deletes a group by ID.
     *
     * @param id ID of the group to delete
     */
    @Operation(summary = "Delete a group", description = "Deletes a group by its ID. Immutable groups (like ALL) cannot be deleted.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Group successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Attempt to delete an immutable group"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @DeleteMapping("/{id}")
    void deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroup(id);
    }

    /**
     * Partially updates group details.
     *
     * @param id              ID of the group to update
     * @param updateGroupDTO  new values for group fields
     * @return updated group
     */
    @Operation(summary = "Update a group", description = "Partially updates group data by its ID. Immutable groups cannot be updated.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group successfully updated"),
            @ApiResponse(responseCode = "403", description = "Attempt to update an immutable group"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @PatchMapping("/{id}")
    ResponseEntity<GroupDTO> updateGroup(@PathVariable("id") Long id, @RequestBody UpdateGroupDTO updateGroupDTO) {
        return ResponseEntity.ok(new GroupDTO(groupService.updateGroup(id, updateGroupDTO)));
    }
}
