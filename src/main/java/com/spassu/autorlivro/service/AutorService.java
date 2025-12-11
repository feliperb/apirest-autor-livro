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

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    // ---------------------------------------------------
    // BUSCAR TODOS (DTO)
    // ---------------------------------------------------
    public List<AutorRecordDto> findAll() {
        return autorRepository.findAll()
                .stream()
                .map(autorMapper::toDto)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------
    // BUSCAR POR ID (DTO) — usado pelo Controller
    // ---------------------------------------------------
    public AutorRecordDto findByIdDto(Long id) {
        Autor autor = findById(id);
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
    	validateAutorDto(dto, null);

        Autor autor = Autor.builder()
                .nome(dto.nome())
                .build();
        autor = autorRepository.save(autor);
        return autorMapper.toDto(autor);
    }

    // ---------------------------------------------------
    // ATUALIZAR
    // ---------------------------------------------------
    public AutorRecordDto update(Long id, AutorRecordDto dto) {
        validateAutorDto(dto, id); // valida DTO + duplicidade

        Autor autor = findById(id);
        autor.setNome(dto.nome());
        autor = autorRepository.save(autor);
        return autorMapper.toDto(autor);
    }


    // ---------------------------------------------------
    // DELETAR
    // ---------------------------------------------------
    public void delete(Long id) {
        Autor autor = findById(id);
        if (!autor.getLivros().isEmpty()) {
            throw new BusinessException("Não é possível deletar um autor que possui livros associados.");
        }
        autorRepository.delete(autor);
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
