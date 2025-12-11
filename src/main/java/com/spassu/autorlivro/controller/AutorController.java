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
import com.spassu.autorlivro.dto.AutorRecordDto;
import com.spassu.autorlivro.dto.AutorRecordDto.AutorView;
import com.spassu.autorlivro.model.Autor;
import com.spassu.autorlivro.service.AutorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @GetMapping
    public ResponseEntity<List<AutorRecordDto>> getAll() {
        return ResponseEntity.ok(autorService.findAll());
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<AutorRecordDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.findByIdDto(id));
    }

    
    @PostMapping
    public ResponseEntity<AutorRecordDto> create(
            @Valid
            @RequestBody
            @JsonView(AutorView.Create.class)
            AutorRecordDto dto
    ) {
    	AutorRecordDto novoAutor = autorService.create(dto);
        return ResponseEntity.status(201).body(novoAutor);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<AutorRecordDto> update(
            @PathVariable Long id,
            @Valid
            @RequestBody
            @JsonView(AutorView.Update.class)
            AutorRecordDto dto
    ) {
    	AutorRecordDto autorAtualizado = autorService.update(id, dto);
        return ResponseEntity.ok(autorAtualizado);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        autorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
