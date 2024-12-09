package com.junho.springevaluation.data.repository;

import com.junho.springevaluation.data.entity.AuthenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenEntityRepository extends JpaRepository<AuthenEntity, Integer> {
    AuthenEntity findByUsername(String username);
    Boolean existsByUsername(String username);

}
