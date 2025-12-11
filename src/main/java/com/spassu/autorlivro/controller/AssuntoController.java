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
import com.spassu.autorlivro.dto.AssuntoRecordDto;
import com.spassu.autorlivro.dto.AssuntoRecordDto.AssuntoView;
import com.spassu.autorlivro.service.AssuntoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assuntos")
@RequiredArgsConstructor
public class AssuntoController {

    private final AssuntoService assuntoService;

    @GetMapping
    public ResponseEntity<List<AssuntoRecordDto>> getAll() {
        return ResponseEntity.ok(assuntoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssuntoRecordDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(assuntoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AssuntoRecordDto> create(
            @Valid @JsonView(AssuntoView.Create.class) @RequestBody AssuntoRecordDto dto
    ) {
        return ResponseEntity.status(201).body(assuntoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssuntoRecordDto> update(
            @PathVariable Long id,
            @Valid @JsonView(AssuntoView.Update.class) @RequestBody AssuntoRecordDto dto
    ) {
        return ResponseEntity.ok(assuntoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assuntoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
