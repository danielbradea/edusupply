package com.bid90.edusupply.service;

import com.bid90.edusupply.dto.group.CreateGroupDTO;
import com.bid90.edusupply.dto.group.GroupDTO;
import com.bid90.edusupply.dto.group.UpdateGroupDTO;
import com.bid90.edusupply.model.Group;

import java.util.List;

public interface GroupService {
    List<Group> getAllGroup();

    Group createGroup(CreateGroupDTO createGroupDTO);

    void deleteGroup(Long id);

    Group updateGroup(Long id, UpdateGroupDTO updateGroupDTO);
}
