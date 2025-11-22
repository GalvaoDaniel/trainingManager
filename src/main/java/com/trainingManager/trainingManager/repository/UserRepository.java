package com.trainingManager.trainingManager.repository;

import com.trainingManager.trainingManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

