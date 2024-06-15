package com.sondev.identityservice.repository;

import com.sondev.identityservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsername(String userName);
    Optional<User> findByUsername(String username);
}
