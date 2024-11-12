package com.team12.user.repository;

import com.team12.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    List<User> findByUsernameContaining(String username);
    User findByEmail(String email);

}
