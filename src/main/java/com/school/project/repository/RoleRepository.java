package com.school.project.repository;

import com.school.project.model.Category;
import com.school.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String role);
    @Query("select r from Role r where r.name = ?1 and (?2 is null or r.id != ?2)")
    Role findExistedName(String name, Long id);
}
