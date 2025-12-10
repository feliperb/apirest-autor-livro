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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.model.Autor;
import com.spassu.autorlivro.model.Livro;
import com.spassu.autorlivro.repository.AutorRepository;

public class AutorServiceTest {

	private AutorRepository autorRepository;
    private AutorService autorService;
    
    @BeforeEach
    void setUp() {
        autorRepository = mock(AutorRepository.class);
        autorService = new AutorService(autorRepository);
    }
    
    @Test
    void findAll_deveRetornarTodosAutores() {
        Autor a1 = new Autor(); a1.setId(1L); a1.setNome("Autor 1");
        Autor a2 = new Autor(); a2.setId(2L); a2.setNome("Autor 2");

        when(autorRepository.findAll()).thenReturn(Arrays.asList(a1, a2));

        List<Autor> result = autorService.findAll();
        assertThat(result).hasSize(2).containsExactly(a1, a2);
    }
    @Test
    void findById_deveRetornarAutorExistente() {
        Autor a = new Autor(); a.setId(1L); a.setNome("Autor 1");
        when(autorRepository.findById(1L)).thenReturn(Optional.of(a));

        Autor result = autorService.findById(1L);
        assertThat(result.getNome()).isEqualTo("Autor 1");
    }

    @Test
    void findById_deveLancarNotFoundExceptionSeNaoExistir() {
        when(autorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autorService.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Autor não encontrado");
    }

    @Test
    void save_deveSalvarAutorValido() {
        Autor a = new Autor(); a.setNome("Novo Autor");

        when(autorRepository.findAll()).thenReturn(List.of());
        when(autorRepository.save(any(Autor.class))).thenAnswer(i -> {
            Autor saved = i.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Autor result = autorService.save(a);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Novo Autor");
    }

    @Test
    void save_deveLancarBusinessExceptionParaNomeDuplicado() {
        Autor existente = new Autor(); existente.setId(1L); existente.setNome("Autor 1");
        when(autorRepository.findAll()).thenReturn(List.of(existente));

        Autor novo = new Autor(); novo.setNome("Autor 1");

        assertThatThrownBy(() -> autorService.save(novo))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe um autor com o mesmo nome");
    }

    @Test
    void update_deveAtualizarAutorExistente() {
        Autor existente = new Autor(); existente.setId(1L); existente.setNome("Autor 1");
        when(autorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(autorRepository.findAll()).thenReturn(List.of(existente));
        when(autorRepository.save(any(Autor.class))).thenAnswer(i -> i.getArgument(0));

        Autor atualizado = new Autor(); atualizado.setNome("Autor Atualizado");
        Autor result = autorService.update(1L, atualizado);

        assertThat(result.getNome()).isEqualTo("Autor Atualizado");
    }

    @Test
    void update_deveLancarBusinessExceptionParaNomeDuplicado() {
        Autor a1 = new Autor(); a1.setId(1L); a1.setNome("Autor 1");
        Autor a2 = new Autor(); a2.setId(2L); a2.setNome("Autor 2");

        when(autorRepository.findById(1L)).thenReturn(Optional.of(a1));
        when(autorRepository.findAll()).thenReturn(List.of(a1, a2));

        Autor atualizado = new Autor(); atualizado.setNome("Autor 2");

        assertThatThrownBy(() -> autorService.update(1L, atualizado))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe outro autor com o mesmo nome");
    }

    @Test
    void delete_deveRemoverAutorSemLivros() {
        Autor a = new Autor(); a.setId(1L); a.setNome("Autor 1");

        when(autorRepository.findById(1L)).thenReturn(Optional.of(a));

        autorService.delete(1L);
        verify(autorRepository, times(1)).delete(a);
    }

    @Test
    void delete_deveLancarBusinessExceptionSeAutorTemLivros() {
        Autor a = new Autor(); a.setId(1L); a.setNome("Autor 1");
        a.getLivros().add(new Livro()); // simulando livro associado

        when(autorRepository.findById(1L)).thenReturn(Optional.of(a));

        assertThatThrownBy(() -> autorService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Não é possível deletar um autor");
    }
}