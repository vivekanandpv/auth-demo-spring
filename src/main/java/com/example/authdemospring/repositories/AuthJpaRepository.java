package com.example.authdemospring.repositories;

import com.example.authdemospring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthJpaRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
