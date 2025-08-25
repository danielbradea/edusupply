package com.bid90.edusupply.service;

import com.bid90.edusupply.dto.group.CreateGroupDTO;
import com.bid90.edusupply.dto.group.UpdateGroupDTO;
import com.bid90.edusupply.exception.GroupException;
import com.bid90.edusupply.exception.UserException;
import com.bid90.edusupply.model.Group;
import com.bid90.edusupply.repository.GroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {


    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> getAllGroup() {
        return groupRepository.findAll();
    }

    @Override
    public Group createGroup(CreateGroupDTO createGroupDTO) {
        checkName(createGroupDTO.getName());
        var group = new Group();
        group.setName(createGroupDTO.getName().toUpperCase());
        group.setDescription(createGroupDTO.getDescription());
        group.setImmutable(false);
        return groupRepository.save(group);
    }


    @Override
    public void deleteGroup(Long id) {
        var group = groupRepository.findById(id).orElseThrow(() -> new GroupException("Group with id " + id + " not found",
                HttpStatus.NOT_FOUND));

        if(group.isImmutable()){
            throw new GroupException("This group is immutable and cannot be deleted.",HttpStatus.FORBIDDEN);
        }
        groupRepository.delete(group);
    }

    @Override
    public Group updateGroup(Long id, UpdateGroupDTO updateGroupDTO) {
        var group = groupRepository.findById(id).orElseThrow(() -> new GroupException("Group with id " + id + " not found",
                HttpStatus.NOT_FOUND));

        if(group.isImmutable()){
            throw new GroupException("This group is immutable and cannot be deleted.",HttpStatus.FORBIDDEN);
        }

        Optional.ofNullable(updateGroupDTO.getName()).ifPresent(name-> {
            checkName(name);
            group.setName(name.toUpperCase());
        });

        Optional.ofNullable(updateGroupDTO.getDescription()).ifPresent(description-> {
            group.setDescription(description.toUpperCase());
        });


       return groupRepository.save(group);
    }


    void checkName(String name) {
        if (name.trim().isEmpty()) {
            throw new UserException("Name cannot be empty", HttpStatus.BAD_REQUEST);
        }
    }
}
