package com.spassu.autorlivro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.spassu.autorlivro.dto.LivroRecordDto;
import com.spassu.autorlivro.exception.BusinessException;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.mapper.LivroMapper;
import com.spassu.autorlivro.model.Assunto;
import com.spassu.autorlivro.model.Autor;
import com.spassu.autorlivro.model.Livro;
import com.spassu.autorlivro.repository.AssuntoRepository;
import com.spassu.autorlivro.repository.AutorRepository;
import com.spassu.autorlivro.repository.LivroRepository;

class LivroServiceTest {

    private LivroRepository livroRepository;
    private AutorRepository autorRepository;
    private AssuntoRepository assuntoRepository;
    private LivroMapper livroMapper;
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroRepository = mock(LivroRepository.class);
        autorRepository = mock(AutorRepository.class);
        assuntoRepository = mock(AssuntoRepository.class);

        livroMapper = new LivroMapper(autorRepository, assuntoRepository);
        livroService = new LivroService(livroRepository, livroMapper);

        when(autorRepository.findById(1L))
                .thenReturn(Optional.of(Autor.builder().id(1L).nome("Autor X").build()));

        when(assuntoRepository.findById(1L))
                .thenReturn(Optional.of(Assunto.builder().id(1L).descricao("Assunto Y").build()));
    }

    @Test
    void create_deveLancarBusinessExceptionQuandoDtoNulo() {
        assertThatThrownBy(() -> livroService.create(null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Dados do livro não podem ser nulos");
    }

    @Test
    void update_deveLancarBusinessExceptionQuandoDtoNulo() {
        Livro existente = Livro.builder()
                .id(1L)
                .titulo("X")
                .editora("E")
                .edicao(1)
                .anoPublicacao("2020")
                .build();

        when(livroRepository.findById(1L)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> livroService.update(1L, null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Dados do livro não podem ser nulos");
    }

    @Test
    void create_deveLancarBusinessExceptionParaTituloDuplicado() {
        LivroRecordDto dto = new LivroRecordDto(
                "Livro X", "Editora X", 1, "2023",
                List.of(1L), List.of(1L),
                null, null, null
        );

        when(livroRepository.existsByTituloIgnoreCaseAndAnoPublicacao("Livro X", "2023"))
                .thenReturn(true);

        assertThatThrownBy(() -> livroService.create(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe um livro com este título e ano de publicação");
    }

    @Test
    void create_deveLancarBusinessExceptionParaAnoNoFuturo() {
        int proximoAno = Year.now().getValue() + 1;

        LivroRecordDto dto = new LivroRecordDto(
                "Livro", "Editora", 1, String.valueOf(proximoAno),
                List.of(1L), List.of(1L),
                null, null, null
        );

        assertThatThrownBy(() -> livroService.create(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Ano de publicação não pode ser no futuro");
    }

    @Test
    void create_deveLancarBusinessExceptionParaEdicaoMuitoAlta() {
        LivroRecordDto dto = new LivroRecordDto(
                "Livro", "Editora", 101, "2023",
                List.of(1L), List.of(1L),
                null, null, null
        );

        assertThatThrownBy(() -> livroService.create(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Edição muito alta");
    }

    @Test
    void create_deveSalvarLivroValido() {
        LivroRecordDto dto = new LivroRecordDto(
                "Livro Novo", "Editora X", 1, "2023",
                List.of(1L), List.of(1L),
                null, null, null
        );

        when(livroRepository.save(any(Livro.class))).thenAnswer(inv -> {
            Livro saved = inv.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        LivroRecordDto result = livroService.create(dto);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.titulo()).isEqualTo("Livro Novo");
    }

    @Test
    void findById_deveLancarNotFoundExceptionSeNaoExistir() {
        when(livroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livroService.findByIdDto(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Livro não encontrado");
    }
}
