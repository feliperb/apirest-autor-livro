package com.spassu.autorlivro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.spassu.autorlivro.dto.AutorRecordDto;
import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.mapper.AutorMapper;
import com.spassu.autorlivro.model.Autor;
import com.spassu.autorlivro.model.Livro;
import com.spassu.autorlivro.repository.AutorRepository;

public class AutorServiceTest {

    private AutorRepository autorRepository;
    private AutorService autorService;
    private AutorMapper autorMapper;

    @BeforeEach
    void setUp() {
        autorRepository = mock(AutorRepository.class);
        autorMapper = mock(AutorMapper.class);
        autorService = new AutorService(autorRepository, autorMapper);
    }

    // --- findAll / findByIdDto ---

    @Test
    void findAll_deveRetornarTodosAutoresComoDto() {
        Autor a1 = Autor.builder().id(1L).nome("Autor 1").livros(new ArrayList<>()).build();
        Autor a2 = Autor.builder().id(2L).nome("Autor 2").livros(new ArrayList<>()).build();

        when(autorRepository.findAll()).thenReturn(Arrays.asList(a1, a2));
        when(autorMapper.toDto(a1)).thenReturn(new AutorRecordDto(1L, "Autor 1"));
        when(autorMapper.toDto(a2)).thenReturn(new AutorRecordDto(1L, "Autor 2"));

        List<AutorRecordDto> result = autorService.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).nome()).isEqualTo("Autor 1");
        assertThat(result.get(1).nome()).isEqualTo("Autor 2");
    }

    @Test
    void findByIdDto_deveRetornarAutorComoDto() {
        Autor a = Autor.builder().id(1L).nome("Autor 1").livros(new ArrayList<>()).build();
        when(autorRepository.findById(1L)).thenReturn(Optional.of(a));
        when(autorMapper.toDto(a)).thenReturn(new AutorRecordDto(1L, "Autor 1"));

        AutorRecordDto result = autorService.findByIdDto(1L);
        assertThat(result.nome()).isEqualTo("Autor 1");
    }

    @Test
    void findByIdDto_deveLancarNotFoundExceptionSeNaoExistir() {
        when(autorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autorService.findByIdDto(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Autor não encontrado");
    }

    @Test
    void findByIdDto_deveLancarBusinessExceptionParaIdNulo() {
        assertThatThrownBy(() -> autorService.findByIdDto(null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("O ID do autor não pode ser nulo");
    }

    // --- create ---

    @Test
    void create_deveSalvarAutorValidoComoDto() {
        AutorRecordDto dto = new AutorRecordDto(1L, "Novo Autor");
        Autor autorSalvo = Autor.builder().id(1L).nome("Novo Autor").build();

        when(autorRepository.existsByNomeIgnoreCase("Novo Autor")).thenReturn(false);
        when(autorRepository.save(any(Autor.class))).thenReturn(autorSalvo);
        when(autorMapper.toDto(autorSalvo)).thenReturn(new AutorRecordDto(1L, "Novo Autor"));

        AutorRecordDto result = autorService.create(dto);

        assertThat(result.nome()).isEqualTo("Novo Autor");
    }

    @Test
    void create_deveLancarBusinessExceptionParaNomeDuplicado() {
        AutorRecordDto dto = new AutorRecordDto(1L, "Autor 1");

        when(autorRepository.existsByNomeIgnoreCase("Autor 1")).thenReturn(true);

        assertThatThrownBy(() -> autorService.create(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe um autor com esse nome.");
    }

    @Test
    void create_deveLancarBusinessExceptionParaDtoNulo() {
        assertThatThrownBy(() -> autorService.create(null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("O nome do autor não pode ser vazio");
    }

    @Test
    void create_deveLancarBusinessExceptionParaNomeVazio() {
        AutorRecordDto dto = new AutorRecordDto(1L, "  ");
        assertThatThrownBy(() -> autorService.create(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("O nome do autor não pode ser vazio");
    }

    // --- update ---

    @Test
    void update_deveAtualizarAutorExistenteComoDto() {
        Autor existente = Autor.builder().id(1L).nome("Autor 1").livros(new ArrayList<>()).build();
        AutorRecordDto dto = new AutorRecordDto(1L, "Autor Atualizado");
        Autor atualizado = Autor.builder().id(1L).nome("Autor Atualizado").build();

        when(autorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(autorRepository.existsByNomeIgnoreCaseAndIdNot("Autor Atualizado", 1L)).thenReturn(false);
        when(autorRepository.save(any(Autor.class))).thenReturn(atualizado);
        when(autorMapper.toDto(atualizado)).thenReturn(new AutorRecordDto(1L, "Autor Atualizado"));

        AutorRecordDto result = autorService.update(1L, dto);
        assertThat(result.nome()).isEqualTo("Autor Atualizado");
    }

    @Test
    void update_deveLancarBusinessExceptionParaNomeDuplicado() {
        AutorRecordDto dto = new AutorRecordDto(1L, "Autor 2");
        Long id = 1L;

        Autor existente = Autor.builder().id(id).nome("Autor 1").build();
        when(autorRepository.findById(id)).thenReturn(Optional.of(existente));
        when(autorRepository.existsByNomeIgnoreCaseAndIdNot("Autor 2", id)).thenReturn(true);

        assertThatThrownBy(() -> autorService.update(id, dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe outro autor com esse nome.");
    }

    @Test
    void update_deveLancarBusinessExceptionParaIdNulo() {
        AutorRecordDto dto = new AutorRecordDto(1L, "Autor X");
        assertThatThrownBy(() -> autorService.update(null, dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("O ID do autor não pode ser nulo");
    }

    @Test
    void update_deveLancarBusinessExceptionParaDtoNulo() {
        assertThatThrownBy(() -> autorService.update(1L, null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("O nome do autor não pode ser vazio");
    }

    @Test
    void update_deveLancarBusinessExceptionParaNomeVazio() {
        Autor existente = Autor.builder().id(1L).nome("Autor 1").build();
        when(autorRepository.findById(1L)).thenReturn(Optional.of(existente));

        AutorRecordDto dto = new AutorRecordDto(1L, "  ");
        assertThatThrownBy(() -> autorService.update(1L, dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("O nome do autor não pode ser vazio");
    }

    // --- delete ---

    @Test
    void delete_deveRemoverAutorSemLivros() {
        Autor a = Autor.builder().id(1L).nome("Autor 1").livros(new ArrayList<>()).build();
        when(autorRepository.findById(1L)).thenReturn(Optional.of(a));

        autorService.delete(1L);

        verify(autorRepository, times(1)).delete(a);
    }

    @Test
    void delete_deveLancarBusinessExceptionSeAutorTemLivros() {
        Autor a = Autor.builder().id(1L).nome("Autor 1").livros(List.of(new Livro())).build();
        when(autorRepository.findById(1L)).thenReturn(Optional.of(a));

        assertThatThrownBy(() -> autorService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Não é possível deletar um autor");
    }

    @Test
    void delete_deveLancarBusinessExceptionParaIdNulo() {
        assertThatThrownBy(() -> autorService.delete(null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("O ID do autor não pode ser nulo");
    }
}
