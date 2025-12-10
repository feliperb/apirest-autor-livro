package com.spassu.autorlivro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.model.Assunto;
import com.spassu.autorlivro.model.Autor;
import com.spassu.autorlivro.model.Livro;
import com.spassu.autorlivro.repository.LivroRepository;

class LivroServiceTest {

    private LivroRepository livroRepository;
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroRepository = mock(LivroRepository.class);
        livroService = new LivroService(livroRepository);
    }

    @Test
    void findAll_deveRetornarTodosLivros() {
        Livro l1 = new Livro(); l1.setId(1L); l1.setTitulo("Livro 1");
        Livro l2 = new Livro(); l2.setId(2L); l2.setTitulo("Livro 2");

        when(livroRepository.findAll()).thenReturn(Arrays.asList(l1, l2));

        List<Livro> result = livroService.findAll();
        assertThat(result).hasSize(2).containsExactly(l1, l2);
    }

    @Test
    void findById_deveRetornarLivroExistente() {
        Livro l = new Livro(); l.setId(1L); l.setTitulo("Livro 1");
        when(livroRepository.findById(1L)).thenReturn(Optional.of(l));

        Livro result = livroService.findById(1L);
        assertThat(result.getTitulo()).isEqualTo("Livro 1");
    }

    @Test
    void findById_deveLancarNotFoundExceptionSeNaoExistir() {
        when(livroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livroService.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Livro não encontrado");
    }

    @Test
    void save_deveSalvarLivroValido() {
        Livro l = new Livro();
        l.setTitulo("Livro Novo");
        l.setEditora("Editora X");
        l.setEdicao(1);
        l.setAnoPublicacao("2023");
        l.setAutores(Set.of(new Autor()));
        l.setAssuntos(Set.of(new Assunto()));

        when(livroRepository.save(any(Livro.class))).thenAnswer(i -> {
            Livro saved = i.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Livro result = livroService.save(l);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitulo()).isEqualTo("Livro Novo");
    }

    @Test
    void save_deveLancarBusinessExceptionParaTituloVazio() {
        Livro l = new Livro();
        l.setTitulo("");
        l.setEditora("Editora X");
        l.setEdicao(1);
        l.setAnoPublicacao("2023");

        assertThatThrownBy(() -> livroService.save(l))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Título do livro não pode ser vazio");
    }

    @Test
    void save_deveLancarBusinessExceptionParaAnoInvalido() {
        Livro l = new Livro();
        l.setTitulo("Livro 1");
        l.setEditora("Editora X");
        l.setEdicao(1);
        l.setAnoPublicacao("20AB");

        assertThatThrownBy(() -> livroService.save(l))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Ano de publicação inválido");
    }

    @Test
    void save_deveLancarBusinessExceptionParaEditoraVazia() {
        Livro l = new Livro();
        l.setTitulo("Livro 1");
        l.setEditora("");
        l.setEdicao(1);
        l.setAnoPublicacao("2023");

        assertThatThrownBy(() -> livroService.save(l))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Editora não pode ser vazia");
    }

    @Test
    void save_deveLancarBusinessExceptionParaEdicaoInvalida() {
        Livro l = new Livro();
        l.setTitulo("Livro 1");
        l.setEditora("Editora X");
        l.setEdicao(0);
        l.setAnoPublicacao("2023");

        assertThatThrownBy(() -> livroService.save(l))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Edição inválida");
    }

    @Test
    void update_deveAtualizarLivroExistente() {
        Livro existente = new Livro();
        existente.setId(1L);
        existente.setTitulo("Livro Antigo");
        existente.setEditora("Editora X");
        existente.setEdicao(1);
        existente.setAnoPublicacao("2022");

        when(livroRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(livroRepository.save(any(Livro.class))).thenAnswer(i -> i.getArgument(0));

        Livro atualizado = new Livro();
        atualizado.setTitulo("Livro Atualizado");
        atualizado.setEditora("Editora Y");
        atualizado.setEdicao(2);
        atualizado.setAnoPublicacao("2023");

        Livro result = livroService.update(1L, atualizado);
        assertThat(result.getTitulo()).isEqualTo("Livro Atualizado");
        assertThat(result.getEditora()).isEqualTo("Editora Y");
        assertThat(result.getEdicao()).isEqualTo(2);
        assertThat(result.getAnoPublicacao()).isEqualTo("2023");
    }

    @Test
    void delete_deveRemoverLivroExistente() {
        Livro l = new Livro(); l.setId(1L);
        when(livroRepository.findById(1L)).thenReturn(Optional.of(l));

        livroService.delete(1L);
        verify(livroRepository, times(1)).delete(l);
    }

    @Test
    void delete_deveLancarNotFoundExceptionSeNaoExistir() {
        when(livroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livroService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Livro não encontrado");
    }
}