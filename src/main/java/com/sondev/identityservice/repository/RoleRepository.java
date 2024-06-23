package com.sondev.identityservice.repository;

import com.sondev.identityservice.entity.Permission;
import com.sondev.identityservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
