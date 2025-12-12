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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "Endpoints para gerenciamento de livros")
@Slf4j
public class LivroController {

    private final LivroService livroService;

    // ---------------------------------------------------------
    // LISTAR TODOS
    // ---------------------------------------------------------
    @Operation(
            summary = "Listar todos os livros",
            description = "Retorna uma lista de todos os livros cadastrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<List<LivroRecordDto>> getAll() {
    	log.info("[LIVROS][GET] Listando todos os livros");
        return ResponseEntity.ok(livroService.findAll());
    }

    // ---------------------------------------------------------
    // BUSCAR POR ID
    // ---------------------------------------------------------
    @Operation(
            summary = "Buscar livro por ID",
            description = "Retorna um livro específico pelo seu identificador",
            parameters = {
                    @Parameter(name = "id", description = "ID do livro", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Livro encontrado"),
                    @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                            content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<LivroRecordDto> getById(@PathVariable Long id) {
    	log.info("[LIVROS][GET] Buscando livro por ID: {}", id);
        return ResponseEntity.ok(livroService.findByIdDto(id));
    }

    // ---------------------------------------------------------
    // CRIAR
    // ---------------------------------------------------------
    @Operation(
            summary = "Cadastrar um novo livro",
            description = "Cria e retorna um novo livro a partir dos dados enviados",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos",
                            content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping
    public ResponseEntity<LivroRecordDto> create(
            @Valid @JsonView(BookView.Create.class)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação do livro",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LivroRecordDto.class))
            )
            @RequestBody LivroRecordDto dto
    ) {
    	 log.info("[LIVROS][POST] Criando livro: {}", dto);
         LivroRecordDto salvo = livroService.create(dto);
         log.info("[LIVROS][POST] Livro criado com ID: {}", salvo.id());
        return ResponseEntity.status(201).body(salvo);
    }

    // ---------------------------------------------------------
    // ATUALIZAR
    // ---------------------------------------------------------
    @Operation(
            summary = "Atualizar um livro existente",
            description = "Atualiza os dados de um livro pelo ID",
            parameters = {
                    @Parameter(name = "id", description = "ID do livro a ser atualizado", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<LivroRecordDto> update(
            @PathVariable Long id,
            @Valid @JsonView(BookView.Update.class)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados novos do livro",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LivroRecordDto.class))
            )
            @RequestBody LivroRecordDto dto
    ) {
    	log.info("[LIVROS][PUT] Atualizando livro ID {}: novos dados {}", id, dto);
        LivroRecordDto atualizado = livroService.update(id, dto);
        log.info("[LIVROS][PUT] Livro ID {} atualizado com sucesso", id);
        return ResponseEntity.ok(atualizado);
    }

    // ---------------------------------------------------------
    // DELETAR
    // ---------------------------------------------------------
    @Operation(
            summary = "Excluir um livro",
            description = "Remove um livro do sistema pelo ID",
            parameters = {
                    @Parameter(name = "id", description = "ID do livro a ser deletado", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	log.info("[LIVROS][DELETE] Deletando livro ID {}", id);
        livroService.delete(id);
        log.info("[LIVROS][DELETE] Livro ID {} deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}
