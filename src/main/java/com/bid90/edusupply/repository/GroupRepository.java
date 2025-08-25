package com.bid90.edusupply.repository;

import com.bid90.edusupply.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long>{

    @Query("SELECT g FROM Group g WHERE g.name = :name")
    Optional<Group> findByName(String name);
}
