package com.spassu.autorlivro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.model.Livro;
import com.spassu.autorlivro.repository.LivroRepository;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> findAll() {
        return livroRepository.findAll();
    }

    public Livro findById(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado com id: " + id));
    }

    public Livro save(Livro livro) {
    	validarLivro(livro);
        return livroRepository.save(livro);
    }

    public Livro update(Long id, Livro livroAtualizado) {
        Livro livro = findById(id);
        validarLivro(livroAtualizado);
        livro.setTitulo(livroAtualizado.getTitulo());
        livro.setEditora(livroAtualizado.getEditora());
        livro.setEdicao(livroAtualizado.getEdicao());
        livro.setAnoPublicacao(livroAtualizado.getAnoPublicacao());
        livro.setAutores(livroAtualizado.getAutores());
        livro.setAssuntos(livroAtualizado.getAssuntos());
        return livroRepository.save(livro);
    }

    public void delete(Long id) {
        Livro livro = findById(id);
        livroRepository.delete(livro);
    }
    
    
    private void validarLivro(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new BusinessException("Título do livro não pode ser vazio");
        }
        if (livro.getAnoPublicacao() == null || !livro.getAnoPublicacao().matches("\\d{4}")) {
            throw new BusinessException("Ano de publicação inválido");
        }
        if (livro.getEditora() == null || livro.getEditora().isBlank()) {
            throw new BusinessException("Editora não pode ser vazia");
        }
        if (livro.getEdicao() == null || livro.getEdicao() <= 0) {
            throw new BusinessException("Edição inválida");
        }
    }
}
