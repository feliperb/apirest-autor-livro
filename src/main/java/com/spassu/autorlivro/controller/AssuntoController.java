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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assuntos")
@RequiredArgsConstructor
@Tag(name = "Assuntos", description = "Endpoints para gerenciamento de assuntos")
public class AssuntoController {

    private final AssuntoService assuntoService;

    @GetMapping
    @Operation(
        summary = "Listar todos os assuntos",
        description = "Retorna uma lista com todos os assuntos cadastrados"
    )
    @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso")
    public ResponseEntity<List<AssuntoRecordDto>> getAll() {
        return ResponseEntity.ok(assuntoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar assunto por ID",
        description = "Retorna os dados de um assunto específico"
    )
    @ApiResponse(responseCode = "200", description = "Assunto encontrado")
    @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
    public ResponseEntity<AssuntoRecordDto> getById(
            @Parameter(description = "ID do assunto", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(assuntoService.findById(id));
    }

    @PostMapping
    @Operation(
        summary = "Criar um novo assunto",
        description = "Cadastra um novo assunto com a descrição informada"
    )
    @ApiResponse(responseCode = "201", description = "Assunto criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<AssuntoRecordDto> create(
            @Valid
            @JsonView(AssuntoView.Create.class)
            @RequestBody AssuntoRecordDto dto
    ) {
        return ResponseEntity.status(201).body(assuntoService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar assunto",
        description = "Atualiza a descrição de um assunto existente"
    )
    @ApiResponse(responseCode = "200", description = "Assunto atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
    public ResponseEntity<AssuntoRecordDto> update(
            @Parameter(description = "ID do assunto", example = "1")
            @PathVariable Long id,

            @Valid
            @JsonView(AssuntoView.Update.class)
            @RequestBody AssuntoRecordDto dto
    ) {
        return ResponseEntity.ok(assuntoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir assunto",
        description = "Remove o assunto informado caso não possua livros associados"
    )
    @ApiResponse(responseCode = "204", description = "Assunto removido com sucesso")
    @ApiResponse(responseCode = "400", description = "Não é possível remover assunto com livros associados")
    @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do assunto", example = "1")
            @PathVariable Long id
    ) {
        assuntoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
