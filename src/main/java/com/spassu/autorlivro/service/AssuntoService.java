package com.spassu.autorlivro.service;

import java.util.List;

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
    private final AssuntoMapper mapper;

    public List<Assunto> findAll() {
        return repository.findAll();
    }

    public Assunto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assunto não encontrado com id: " + id));
    }

    public Assunto create(AssuntoRecordDto dto) {
        Assunto assunto = mapper.toEntity(dto);
        validarAssunto(assunto);

        // Verifica duplicidade
        if (repository.existsByDescricaoIgnoreCase(assunto.getDescricao())) {
            throw new BusinessException("Já existe um assunto com a mesma descrição");
        }

        return repository.save(assunto);
    }

    public Assunto update(Long id, AssuntoRecordDto dto) {
        Assunto assunto = findById(id);
        mapper.updateEntityFromDto(assunto, dto);
        validarAssunto(assunto);

        // Verifica duplicidade ignorando o próprio ID
        if (repository.existsByDescricaoIgnoreCaseAndIdNot(assunto.getDescricao(), id)) {
            throw new BusinessException("Já existe outro assunto com a mesma descrição");
        }

        return repository.save(assunto);
    }

    public void delete(Long id) {
        Assunto assunto = findById(id);

        // Verifica livros associados
        if (!assunto.getLivros().isEmpty()) {
            throw new BusinessException("Não é possível deletar um assunto que possui livros associados");
        }

        repository.delete(assunto);
    }

    private void validarAssunto(Assunto assunto) {
        if (assunto.getDescricao() == null || assunto.getDescricao().isBlank()) {
            throw new BusinessException("Descrição do assunto não pode ser vazia");
        }
    }
}
