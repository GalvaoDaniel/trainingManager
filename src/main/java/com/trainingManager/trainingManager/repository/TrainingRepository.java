package com.trainingManager.trainingManager.repository;

import com.trainingManager.trainingManager.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
