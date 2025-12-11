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

import com.spassu.autorlivro.dto.AssuntoRecordDto;
import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.mapper.AssuntoMapper;
import com.spassu.autorlivro.model.Assunto;
import com.spassu.autorlivro.model.Livro;
import com.spassu.autorlivro.repository.AssuntoRepository;

class AssuntoServiceTest {

    private AssuntoRepository assuntoRepository;
    private AssuntoMapper mapper;
    private AssuntoService assuntoService;

    @BeforeEach
    void setUp() {
        assuntoRepository = mock(AssuntoRepository.class);
        mapper = new AssuntoMapper();
        assuntoService = new AssuntoService(assuntoRepository, mapper);
    }

    
    @Test
    void findAll_deveRetornarListaDeDto() {
        Assunto a1 = Assunto.builder().id(1L).descricao("Assunto 1").build();
        Assunto a2 = Assunto.builder().id(2L).descricao("Assunto 2").build();

        when(assuntoRepository.findAll()).thenReturn(Arrays.asList(a1, a2));

        List<AssuntoRecordDto> result = assuntoService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).descricao()).isEqualTo("Assunto 1");
        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).descricao()).isEqualTo("Assunto 2");
    }

    
    @Test
    void findById_deveRetornarAssuntoExistente() {
        Assunto a = Assunto.builder().id(1L).descricao("Assunto 1").build();
        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(a));

        AssuntoRecordDto result = assuntoService.findById(1L);

        assertThat(result.descricao()).isEqualTo("Assunto 1");
    }

    @Test
    void findById_deveLancarNotFoundExceptionSeNaoExistir() {
        when(assuntoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assuntoService.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Assunto não encontrado");
    }

    
    @Test
    void create_deveSalvarERetornarDto() {
        AssuntoRecordDto dto = new AssuntoRecordDto(null, "Novo Assunto");

        when(assuntoRepository.existsByDescricaoIgnoreCase("Novo Assunto")).thenReturn(false);

        when(assuntoRepository.save(any(Assunto.class))).thenAnswer(inv -> {
            Assunto saved = inv.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        AssuntoRecordDto result = assuntoService.create(dto);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.descricao()).isEqualTo("Novo Assunto");
    }

    @Test
    void create_deveLancarBusinessExceptionParaDescricaoVazia() {
        AssuntoRecordDto dto = new AssuntoRecordDto(null, "   ");

        assertThatThrownBy(() -> assuntoService.create(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("A descrição do assunto não pode ser vazia");
    }

    
    @Test
    void update_deveAtualizarAssuntoExistente() {
        Assunto existente = Assunto.builder().id(1L).descricao("Antigo").build();

        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(assuntoRepository.existsByDescricaoIgnoreCaseAndIdNot("Atualizado", 1L))
                .thenReturn(false);

        when(assuntoRepository.save(any(Assunto.class))).thenAnswer(inv -> inv.getArgument(0));

        AssuntoRecordDto dto = new AssuntoRecordDto(null, "Atualizado");

        AssuntoRecordDto result = assuntoService.update(1L, dto);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.descricao()).isEqualTo("Atualizado");
    }

    @Test
    void update_deveLancarBusinessExceptionParaDescricaoVazia() {
        Assunto existente = Assunto.builder().id(1L).descricao("Assunto 1").build();

        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(existente));

        AssuntoRecordDto dto = new AssuntoRecordDto(1L, "");

        assertThatThrownBy(() -> assuntoService.update(1L, dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("A descrição do assunto não pode ser vazia");
    }


    
    @Test
    void delete_deveRemoverAssuntoSemLivros() {
        Assunto a = Assunto.builder().id(1L).livros(List.of()).build();
        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(a));

        assuntoService.delete(1L);

        verify(assuntoRepository, times(1)).delete(a);
    }

    @Test
    void delete_deveLancarBusinessExceptionSeAssuntoTemLivros() {
        Assunto a = Assunto.builder().id(1L).livros(List.of(new Livro())).build();

        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(a));

        assertThatThrownBy(() -> assuntoService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Não é possível deletar um assunto que possui livros associados");
    }
}
