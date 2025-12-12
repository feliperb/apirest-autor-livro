package com.spassu.autorlivro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spassu.autorlivro.dto.AutorRecordDto;
import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.mapper.AutorMapper;
import com.spassu.autorlivro.model.Autor;
import com.spassu.autorlivro.repository.AutorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    // ---------------------------------------------------
    // BUSCAR TODOS (DTO)
    // ---------------------------------------------------
    public List<AutorRecordDto> findAll() {
        log.info("[AUTOR] Buscando todos os autores");

        List<AutorRecordDto> autores = autorRepository.findAll()
                .stream()
                .map(autorMapper::toDto)
                .collect(Collectors.toList());

        log.info("[AUTOR] {} autores encontrados", autores.size());
        return autores;
    }

    // ---------------------------------------------------
    // BUSCAR POR ID (DTO) — usado pelo Controller
    // ---------------------------------------------------
    public AutorRecordDto findByIdDto(Long id) {
        log.info("[AUTOR] Buscando autor DTO por ID {}", id);

        Autor autor = findById(id);

        log.info("[AUTOR] Autor encontrado: {}", autor.getNome());
        return autorMapper.toDto(autor);
    }
    
    // ---------------------------------------------------
    // BUSCAR POR ID (ENTITY) - uso interno
    // ---------------------------------------------------
    private Autor findById(Long id) {
        if (id == null) {
            throw new BusinessException("O ID do autor não pode ser nulo");
        }
        return autorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor não encontrado com id: " + id));
    }

    // ---------------------------------------------------
    // CRIAR
    // ---------------------------------------------------
    public AutorRecordDto create(AutorRecordDto dto) {
        log.info("[AUTOR] Criando novo autor");

        validateAutorDto(dto, null);

        Autor autor = Autor.builder()
                .nome(dto.nome())
                .build();

        autor = autorRepository.save(autor);

        log.info("[AUTOR] Autor criado com sucesso: {}", autor.getNome());
        return autorMapper.toDto(autor);
    }

    // ---------------------------------------------------
    // ATUALIZAR
    // ---------------------------------------------------
    public AutorRecordDto update(Long id, AutorRecordDto dto) {
        log.info("[AUTOR] Atualizando autor ID {}", id);

        validateAutorDto(dto, id);

        Autor autor = findById(id);
        autor.setNome(dto.nome());

        autor = autorRepository.save(autor);

        log.info("[AUTOR] Autor atualizado com sucesso: {}", autor.getNome());
        return autorMapper.toDto(autor);
    }


    // ---------------------------------------------------
    // DELETAR
    // ---------------------------------------------------
    public void delete(Long id) {
        log.info("[AUTOR] Removendo autor ID {}", id);

        Autor autor = findById(id);

        if (!autor.getLivros().isEmpty()) {
            log.warn("[AUTOR] Tentativa de excluir autor com livros associados: {}", autor.getNome());
            throw new BusinessException("Não é possível deletar um autor que possui livros associados.");
        }

        autorRepository.delete(autor);

        log.info("[AUTOR] Autor removido com sucesso: {}", autor.getNome());
    }
    
    
    // --------------------------------------------------
    // VALIDAÇÕES REGRAS DE NEGÓCIO - Autor
    // --------------------------------------------------
    public void validateAutorDto(AutorRecordDto dto, Long id) {
        if (dto == null || dto.nome() == null || dto.nome().isBlank()) {
            throw new BusinessException("O nome do autor não pode ser vazio");
        }

        boolean existe;
        if (id == null) {
            // Create
            existe = autorRepository.existsByNomeIgnoreCase(dto.nome());
        } else {
            // Update
            existe = autorRepository.existsByNomeIgnoreCaseAndIdNot(dto.nome(), id);
        }

        if (existe) {
            throw new BusinessException(
                id == null ?
                "Já existe um autor com esse nome." :
                "Já existe outro autor com esse nome."
            );
        }
    }

}
