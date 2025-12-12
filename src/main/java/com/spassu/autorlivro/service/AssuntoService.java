package com.spassu.autorlivro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spassu.autorlivro.dto.AssuntoRecordDto;
import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.mapper.AssuntoMapper;
import com.spassu.autorlivro.model.Assunto;
import com.spassu.autorlivro.repository.AssuntoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssuntoService {

    private final AssuntoRepository repository;
    private final AssuntoMapper assuntoMapper;

    // --------------------------------------------------
    // LISTAR TODOS
    // --------------------------------------------------
    public List<AssuntoRecordDto> findAll() {
    	log.info("[ASSUNTOS][SERVICE][GET] Buscando todos os assuntos");

        List<AssuntoRecordDto> lista = repository.findAll()
                .stream()
                .map(assuntoMapper::toDto)
                .collect(Collectors.toList());

        log.info("[ASSUNTOS][SERVICE][GET] {} assuntos encontrados", lista.size());
        return lista;
    }

    // --------------------------------------------------
    // BUSCAR POR ID (DTO)
    // --------------------------------------------------
    public AssuntoRecordDto findById(Long id) {
        log.info("[ASSUNTOS][SERVICE][GET] Buscando assunto ID {}", id);

        if (id == null)
            throw new BusinessException("O ID do assunto não pode ser nulo");

        Assunto assunto = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ASSUNTOS][SERVICE][GET] Assunto ID {} não encontrado", id);
                    return new NotFoundException("Assunto não encontrado com id: " + id);
                });

        log.info("[ASSUNTOS][SERVICE][GET] Assunto ID {} encontrado", id);
        return assuntoMapper.toDto(assunto);
    }

    // --------------------------------------------------
    // CRIAR
    // --------------------------------------------------
    public AssuntoRecordDto create(AssuntoRecordDto dto) {
    	log.info("[ASSUNTOS][SERVICE][POST] Criando novo assunto: {}", dto);
    	validarDto(dto);
    	String descricao = dto.descricao().trim();
    	
    	if (repository.existsByDescricaoIgnoreCase(descricao)) {
            log.warn("[ASSUNTOS][SERVICE][POST] Já existe assunto com descrição '{}'", descricao);
            throw new BusinessException("Já existe um assunto com a mesma descrição");
        }
    	
        Assunto assunto = assuntoMapper.toEntity(dto);
        assunto.setDescricao(descricao);
        Assunto salvo = repository.save(assunto);
        log.info("[ASSUNTOS][SERVICE][POST] Assunto criado com ID {}", salvo.getId());
        
        return assuntoMapper.toDto(salvo);
    }

    // --------------------------------------------------
    // ATUALIZAR
    // --------------------------------------------------
    public AssuntoRecordDto update(Long id, AssuntoRecordDto dto) {
    	log.info("[ASSUNTOS][SERVICE][PUT] Atualizando assunto ID {}: novos dados {}", id, dto);
    	
    	if (id == null)
            throw new BusinessException("O ID do assunto não pode ser nulo");
        validarDto(dto);
        
        Assunto assunto = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ASSUNTOS][SERVICE][PUT] Assunto ID {} não encontrado", id);
                    return new NotFoundException("Assunto não encontrado com id: " + id);
                });

        String novaDescricao = dto.descricao().trim();
        
        if (repository.existsByDescricaoIgnoreCaseAndIdNot(novaDescricao, id)) {
            throw new BusinessException("Já existe outro assunto com a mesma descrição");
        }

        assuntoMapper.updateEntityFromDto(assunto, dto);
        assunto.setDescricao(novaDescricao);
        Assunto atualizado = repository.save(assunto);
        log.info("[ASSUNTOS][SERVICE][PUT] Assunto ID {} atualizado com sucesso", id);
        
        return assuntoMapper.toDto(atualizado);
    }

    // --------------------------------------------------
    // DELETAR
    // --------------------------------------------------
    public void delete(Long id) {
    	log.info("[ASSUNTOS][SERVICE][DELETE] Deletando assunto ID {}", id);
    	if (id == null)
            throw new BusinessException("O ID do assunto não pode ser nulo");

    	Assunto assunto = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ASSUNTOS][SERVICE][DELETE] Assunto ID {} não encontrado", id);
                    return new NotFoundException("Assunto não encontrado com id: " + id);
                });

    	if (!assunto.getLivros().isEmpty()) {
            log.warn("[ASSUNTOS][SERVICE][DELETE] Assunto ID {} possui livros associados. Não é possível deletar.", id);
            throw new BusinessException("Não é possível deletar um assunto que possui livros associados");
        }

        repository.delete(assunto);
        log.info("[ASSUNTOS][SERVICE][DELETE] Assunto ID {} deletado com sucesso", id);
    }

    
    // --------------------------------------------------
    // VALIDACOES
    // --------------------------------------------------
    private void validarDto(AssuntoRecordDto dto) {
        if (dto == null)
            throw new BusinessException("Os dados do assunto não podem ser nulos");

        if (dto.descricao() == null || dto.descricao().isBlank()) {
            throw new BusinessException("A descrição do assunto não pode ser vazia");
        }
    }
}
