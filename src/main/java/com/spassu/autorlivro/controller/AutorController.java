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
import com.spassu.autorlivro.service.AutorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Endpoints para gerenciamento de autores")
public class AutorController {

    private final AutorService autorService;

    @GetMapping
    @Operation(
        summary = "Listar todos os autores",
        description = "Retorna uma lista contendo todos os autores cadastrados"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<AutorRecordDto>> getAll() {
        return ResponseEntity.ok(autorService.findAll());
    }


    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar autor por ID",
        description = "Retorna os dados de um autor específico pelo seu ID"
    )
    @ApiResponse(responseCode = "200", description = "Autor encontrado")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    public ResponseEntity<AutorRecordDto> getById(
            @Parameter(description = "ID do autor", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(autorService.findByIdDto(id));
    }


    @PostMapping
    @Operation(
        summary = "Criar um novo autor",
        description = "Cria um novo autor com base nos dados enviados"
    )
    @ApiResponse(responseCode = "201", description = "Autor criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
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
    @Operation(
        summary = "Atualizar autor",
        description = "Atualiza os dados de um autor existente"
    )
    @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    public ResponseEntity<AutorRecordDto> update(
            @Parameter(description = "ID do autor", example = "1")
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
    @Operation(
        summary = "Excluir autor",
        description = "Remove o autor informado pelo ID"
    )
    @ApiResponse(responseCode = "204", description = "Autor removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do autor", example = "1")
            @PathVariable Long id
    ) {
        autorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
