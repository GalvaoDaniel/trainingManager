package com.trainingManager.trainingManager.controller;

import com.trainingManager.trainingManager.model.Training;
import com.trainingManager.trainingManager.repository.TrainingRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {

    private final TrainingRepository repository;

    public TrainingController(TrainingRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Training> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Training> get(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Training> create(@Valid @RequestBody Training training) {
        if (training.getId() != null) {
            // Prevent overriding ids on create
            training.setId(null);
        }
        Training saved = repository.save(training);
        return ResponseEntity.created(URI.create("/api/trainings/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Training> update(@PathVariable Long id, @Valid @RequestBody Training training) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setAddress(training.getAddress());
                    existing.setInstructor(training.getInstructor());
                    existing.setSubject(training.getSubject());
                    existing.setDurationMinutes(training.getDurationMinutes());
                    Training saved = repository.save(existing);
                    return new ResponseEntity<>(saved, HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repository.findById(id)
                .map(existing -> {
                    repository.delete(existing);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
