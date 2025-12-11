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

import com.fasterxml.jackson.annotation.JsonView;
import com.spassu.autorlivro.dto.LivroRecordDto;
import com.spassu.autorlivro.dto.LivroRecordDto.BookView;
import com.spassu.autorlivro.service.LivroService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @GetMapping
    public ResponseEntity<List<LivroRecordDto>> getAll() {
        return ResponseEntity.ok(livroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroRecordDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.findByIdDto(id));
    }

    @PostMapping
    public ResponseEntity<LivroRecordDto> create(
            @Valid @JsonView(BookView.Create.class) @RequestBody LivroRecordDto dto
    ) {
        LivroRecordDto salvo = livroService.create(dto);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroRecordDto> update(
            @PathVariable Long id,
            @Valid @JsonView(BookView.Update.class) @RequestBody LivroRecordDto dto
    ) {
        LivroRecordDto atualizado = livroService.update(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
