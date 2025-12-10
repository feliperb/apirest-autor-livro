package com.spassu.autorlivro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spassu.autorlivro.model.Assunto;
import com.spassu.autorlivro.service.AssuntoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assuntos")
@RequiredArgsConstructor
public class AssuntoController {

    private final AssuntoService assuntoService;

    @GetMapping
    public ResponseEntity<List<Assunto>> getAll() {
        return ResponseEntity.ok(assuntoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assunto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(assuntoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Assunto> create(@Valid @RequestBody Assunto assunto) {
        return ResponseEntity.status(201).body(assuntoService.save(assunto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assunto> update(@PathVariable Long id, @Valid @RequestBody Assunto assunto) {
        return ResponseEntity.ok(assuntoService.update(id, assunto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assuntoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
