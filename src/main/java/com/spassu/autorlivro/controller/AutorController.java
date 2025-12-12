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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Endpoints para gerenciamento de autores")
@Slf4j
public class AutorController {

    private final AutorService autorService;

    @GetMapping
    @Operation(
        summary = "Listar todos os autores",
        description = "Retorna uma lista contendo todos os autores cadastrados"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<AutorRecordDto>> getAll() {
    	log.info("[AUTORES][GET] Listando todos os autores");
        return ResponseEntity.ok(autorService.findAll());
    }


    public ResponseEntity<AutorRecordDto> getById(
            @Parameter(description = "ID do autor", example = "1")
            @PathVariable Long id
    ) {
        log.info("[AUTORES][GET] Buscando autor por ID {}", id);
        AutorRecordDto autor = autorService.findByIdDto(id);
        log.info("[AUTORES][GET] Autor encontrado: {}", autor);
        return ResponseEntity.ok(autor);
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
    	log.info("[AUTORES][POST] Criando autor: {}", dto);
        AutorRecordDto novoAutor = autorService.create(dto);
        log.info("[AUTORES][POST] Autor criado com ID {}", novoAutor.id());
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
    	log.info("[AUTORES][PUT] Atualizando autor ID {} com dados {}", id, dto);
        AutorRecordDto autorAtualizado = autorService.update(id, dto);
        log.info("[AUTORES][PUT] Autor ID {} atualizado com sucesso", id);
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
    	log.info("[AUTORES][DELETE] Removendo autor ID {}", id);
        autorService.delete(id);
        log.info("[AUTORES][DELETE] Autor ID {} removido com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}
