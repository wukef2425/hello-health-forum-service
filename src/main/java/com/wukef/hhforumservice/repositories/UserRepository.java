package com.wukef.hhforumservice.repositories;

import com.wukef.hhforumservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Basic CRUD operations are already provided by JpaRepository.
}