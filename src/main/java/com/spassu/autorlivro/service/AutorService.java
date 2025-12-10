package com.spassu.autorlivro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.model.Autor;
import com.spassu.autorlivro.repository.AutorRepository;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> findAll() {
        return autorRepository.findAll();
    }

    public Autor findById(Long id) {
    	return autorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor não encontrado com id: " + id));
    }

    public Autor save(Autor autor) {
    	if (autorRepository.findAll().stream().anyMatch(a -> a.getNome().equalsIgnoreCase(autor.getNome()))) {
            throw new BusinessException("Já existe um autor com o mesmo nome");
        }
        return autorRepository.save(autor);
    }

    public Autor update(Long id, Autor autorAtualizado) {
        Autor autor = findById(id);
        if (autorRepository.findAll().stream()
                .anyMatch(a -> !a.getId().equals(id) && a.getNome().equalsIgnoreCase(autorAtualizado.getNome()))) {
            throw new BusinessException("Já existe outro autor com o mesmo nome");
        }
        autor.setNome(autorAtualizado.getNome());
        return autorRepository.save(autor);
    }

    public void delete(Long id) {
        Autor autor = findById(id);
        if (!autor.getLivros().isEmpty()) {
            throw new BusinessException("Não é possível deletar um autor que possui livros associados");
        }
        autorRepository.delete(autor);
    }
}
