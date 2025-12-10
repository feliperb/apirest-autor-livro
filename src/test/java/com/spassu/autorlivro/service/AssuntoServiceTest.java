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
import com.spassu.autorlivro.model.Livro;
import com.spassu.autorlivro.repository.AssuntoRepository;

class AssuntoServiceTest {

    private AssuntoRepository assuntoRepository;
    private AssuntoService assuntoService;

    @BeforeEach
    void setUp() {
        assuntoRepository = mock(AssuntoRepository.class);
        assuntoService = new AssuntoService(assuntoRepository);
    }

    @Test
    void findAll_deveRetornarTodosAssuntos() {
        Assunto a1 = new Assunto(); a1.setId(1L); a1.setDescricao("Assunto 1");
        Assunto a2 = new Assunto(); a2.setId(2L); a2.setDescricao("Assunto 2");

        when(assuntoRepository.findAll()).thenReturn(Arrays.asList(a1, a2));

        List<Assunto> result = assuntoService.findAll();
        assertThat(result).hasSize(2).containsExactly(a1, a2);
    }

    @Test
    void findById_deveRetornarAssuntoExistente() {
        Assunto a = new Assunto(); a.setId(1L); a.setDescricao("Assunto 1");
        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(a));

        Assunto result = assuntoService.findById(1L);
        assertThat(result.getDescricao()).isEqualTo("Assunto 1");
    }

    @Test
    void findById_deveLancarNotFoundExceptionSeNaoExistir() {
        when(assuntoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assuntoService.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Assunto não encontrado");
    }

    @Test
    void save_deveSalvarAssuntoValido() {
        Assunto a = new Assunto(); a.setDescricao("Novo Assunto");

        when(assuntoRepository.save(any(Assunto.class))).thenAnswer(i -> {
            Assunto saved = i.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Assunto result = assuntoService.save(a);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescricao()).isEqualTo("Novo Assunto");
    }

    @Test
    void save_deveLancarBusinessExceptionParaDescricaoVazia() {
        Assunto a = new Assunto(); a.setDescricao("  ");

        assertThatThrownBy(() -> assuntoService.save(a))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Descrição do assunto não pode ser vazia");
    }

    @Test
    void update_deveAtualizarAssuntoExistente() {
        Assunto existente = new Assunto(); existente.setId(1L); existente.setDescricao("Antigo");
        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(assuntoRepository.save(any(Assunto.class))).thenAnswer(i -> i.getArgument(0));

        Assunto atualizado = new Assunto(); atualizado.setDescricao("Atualizado");
        Assunto result = assuntoService.update(1L, atualizado);

        assertThat(result.getDescricao()).isEqualTo("Atualizado");
    }

    @Test
    void update_deveLancarBusinessExceptionParaDescricaoVazia() {
        Assunto existente = new Assunto(); existente.setId(1L); existente.setDescricao("Assunto 1");
        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(existente));

        Assunto atualizado = new Assunto(); atualizado.setDescricao("");

        assertThatThrownBy(() -> assuntoService.update(1L, atualizado))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Descrição do assunto não pode ser vazia");
    }

    @Test
    void delete_deveRemoverAssuntoSemLivros() {
        Assunto a = new Assunto(); a.setId(1L);
        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(a));

        assuntoService.delete(1L);
        verify(assuntoRepository, times(1)).delete(a);
    }

    @Test
    void delete_deveLancarBusinessExceptionSeAssuntoTemLivros() {
        Assunto a = new Assunto(); a.setId(1L);
        a.setLivros(Set.of(new Livro())); // simulando livro associado

        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(a));

        assertThatThrownBy(() -> assuntoService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Não é possível deletar um assunto");
    }
}
