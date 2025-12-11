package com.spassu.autorlivro.service;

import java.time.Year;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spassu.autorlivro.dto.LivroRecordDto;
import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.mapper.LivroMapper;
import com.spassu.autorlivro.model.Livro;
import com.spassu.autorlivro.repository.LivroRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;

    // --------------------------------------------------
    // LISTAR TODOS
    // --------------------------------------------------
    public List<LivroRecordDto> findAll() {
        return livroRepository.findAll()
                .stream()
                .map(livroMapper::toDto)
                .toList();
    }

	 // --------------------------------------------------
	 // BUSCAR POR ID (DTO) — usado pelo Controller
	 // --------------------------------------------------
	 public LivroRecordDto findByIdDto(Long id) {
	     Livro livro = findById(id);
	     return livroMapper.toDto(livro);
	 }
 
    // --------------------------------------------------
    // BUSCAR POR ID (ENTITY) - uso interno
    // --------------------------------------------------
    private Livro findById(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado com id: " + id));
    }

    // --------------------------------------------------
    // CRIAR
    // --------------------------------------------------
    public LivroRecordDto create(LivroRecordDto dto) {
        if (dto == null) {
            throw new BusinessException("Dados do livro não podem ser nulos");
        }

        Livro livro = livroMapper.toEntity(dto);
        validarLivro(livro, true);

        livroRepository.save(livro);

        return livroMapper.toDto(livro);
    }

    // --------------------------------------------------
    // ATUALIZAR
    // --------------------------------------------------
    public LivroRecordDto update(Long id, LivroRecordDto dto) {
        if (dto == null) {
            throw new BusinessException("Dados do livro não podem ser nulos");
        }

        Livro livro = findById(id);

        livroMapper.updateEntityFromDto(livro, dto);
        validarLivro(livro, false);

        livroRepository.save(livro);

        return livroMapper.toDto(livro);
    }

    // --------------------------------------------------
    // DELETAR
    // --------------------------------------------------
    public void delete(Long id) {
        Livro livro = findById(id);
        livroRepository.delete(livro);
    }
    
    
    // --------------------------------------------------
    // VALIDAÇÕES REGRAS DE NEGÓCIO - Livro
    // --------------------------------------------------
    public void validarLivro(Livro livro, boolean isCreate) {

        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new BusinessException("O título do livro não pode ser vazio");
        }

        if (livro.getAnoPublicacao() == null || !livro.getAnoPublicacao().matches("\\d{4}")) {
            throw new BusinessException("Ano de publicação inválido (esperado 4 dígitos)");
        }

        int ano = Integer.parseInt(livro.getAnoPublicacao());
        int anoAtual = Year.now().getValue();
        if (ano > anoAtual) {
            throw new BusinessException("Ano de publicação não pode ser no futuro");
        }

        if (livro.getEditora() == null || livro.getEditora().isBlank()) {
            throw new BusinessException("A editora não pode ser vazia");
        }

        if (livro.getEdicao() == null || livro.getEdicao() <= 0) {
            throw new BusinessException("Edição precisa ser maior que zero");
        }

        if (livro.getEdicao() > 100) {
            throw new BusinessException("Edição muito alta, verifique o valor");
        }

        if (isCreate &&
            livroRepository.existsByTituloIgnoreCaseAndAnoPublicacao(
                    livro.getTitulo(), livro.getAnoPublicacao())) {

            throw new BusinessException("Já existe um livro com este título e ano de publicação");
        }
    }
}
