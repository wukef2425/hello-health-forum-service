package com.wukef.hhforumservice.repositories;

import com.wukef.hhforumservice.entities.FloorCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorCheckRepository extends JpaRepository<FloorCheck, Integer> {
}