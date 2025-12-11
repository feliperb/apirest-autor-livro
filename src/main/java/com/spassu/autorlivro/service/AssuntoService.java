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

@Service
@RequiredArgsConstructor
public class AssuntoService {

    private final AssuntoRepository repository;
    private final AssuntoMapper assuntoMapper;

    // --------------------------------------------------
    // LISTAR TODOS
    // --------------------------------------------------
    public List<AssuntoRecordDto> findAll() {
    	return repository.findAll()
                .stream()
                .map(assuntoMapper::toDto)
                .collect(Collectors.toList());
    }

    // --------------------------------------------------
    // BUSCAR POR ID (DTO)
    // --------------------------------------------------
    public AssuntoRecordDto findById(Long id) {
    	if (id == null)
            throw new BusinessException("O ID do assunto não pode ser nulo");
            
        Assunto assunto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assunto não encontrado com id: " + id));
        return assuntoMapper.toDto(assunto);
    }

    // --------------------------------------------------
    // CRIAR
    // --------------------------------------------------
    public AssuntoRecordDto create(AssuntoRecordDto dto) {
    	validarDto(dto);
    	String descricao = dto.descricao().trim();
    	
    	if (repository.existsByDescricaoIgnoreCase(descricao)) {
            throw new BusinessException("Já existe um assunto com a mesma descrição");
        }
        Assunto assunto = assuntoMapper.toEntity(dto);
        assunto.setDescricao(descricao);
        Assunto salvo = repository.save(assunto);
        
        return assuntoMapper.toDto(salvo);
    }

    // --------------------------------------------------
    // ATUALIZAR
    // --------------------------------------------------
    public AssuntoRecordDto update(Long id, AssuntoRecordDto dto) {
    	if (id == null)
            throw new BusinessException("O ID do assunto não pode ser nulo");
        validarDto(dto);
        
        Assunto assunto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assunto não encontrado com id: " + id));

        String novaDescricao = dto.descricao().trim();
        
        if (repository.existsByDescricaoIgnoreCaseAndIdNot(novaDescricao, id)) {
            throw new BusinessException("Já existe outro assunto com a mesma descrição");
        }

        assuntoMapper.updateEntityFromDto(assunto, dto);
        assunto.setDescricao(novaDescricao);
        Assunto atualizado = repository.save(assunto);
        return assuntoMapper.toDto(atualizado);
    }

    // --------------------------------------------------
    // DELETAR
    // --------------------------------------------------
    public void delete(Long id) {
    	if (id == null)
            throw new BusinessException("O ID do assunto não pode ser nulo");

        Assunto assunto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assunto não encontrado com id: " + id));

        if (!assunto.getLivros().isEmpty()) {
            throw new BusinessException("Não é possível deletar um assunto que possui livros associados");
        }

        repository.delete(assunto);
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
