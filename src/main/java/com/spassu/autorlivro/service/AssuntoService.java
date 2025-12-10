package com.spassu.autorlivro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.model.Assunto;
import com.spassu.autorlivro.repository.AssuntoRepository;

@Service
public class AssuntoService {

    private final AssuntoRepository assuntoRepository;

    public AssuntoService(AssuntoRepository assuntoRepository) {
        this.assuntoRepository = assuntoRepository;
    }

    public List<Assunto> findAll() {
        return assuntoRepository.findAll();
    }

    public Assunto findById(Long id) {
    	return assuntoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assunto não encontrado com id: " + id));
    }

    public Assunto save(Assunto assunto) {
    	if (assunto.getDescricao() == null || assunto.getDescricao().isBlank()) {
            throw new BusinessException("Descrição do assunto não pode ser vazia");
        }
        return assuntoRepository.save(assunto);
    }

    public Assunto update(Long id, Assunto assuntoAtualizado) {
        Assunto assunto = findById(id);
        if (assuntoAtualizado.getDescricao() == null || assuntoAtualizado.getDescricao().isBlank()) {
            throw new BusinessException("Descrição do assunto não pode ser vazia");
        }
        assunto.setDescricao(assuntoAtualizado.getDescricao());
        return assuntoRepository.save(assunto);
    }

    public void delete(Long id) {
        Assunto assunto = findById(id);
        if (!assunto.getLivros().isEmpty()) {
            throw new BusinessException("Não é possível deletar um assunto associado a livros");
        }
        assuntoRepository.delete(assunto);
    }
}
