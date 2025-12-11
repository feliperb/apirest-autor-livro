package com.spassu.autorlivro.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.spassu.autorlivro.dto.AssuntoRecordDto;
import com.spassu.autorlivro.dto.AutorRecordDto;
import com.spassu.autorlivro.dto.LivroRecordDto;
import com.spassu.autorlivro.exception.NotFoundException;
import com.spassu.autorlivro.model.Assunto;
import com.spassu.autorlivro.model.Autor;
import com.spassu.autorlivro.model.Livro;
import com.spassu.autorlivro.repository.AssuntoRepository;
import com.spassu.autorlivro.repository.AutorRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LivroMapper {

    private final AutorRepository autorRepository;
    private final AssuntoRepository assuntoRepository;

    // ---------------------------------------------------
    // DTO → Entity (Create)
    // ---------------------------------------------------
    public Livro toEntity(LivroRecordDto dto) {

        List<Autor> autores = dto.idsAutores().stream()
                .map(id -> autorRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Autor não encontrado com id: " + id)))
                .toList();

        List<Assunto> assuntos = dto.idsAssuntos().stream()
                .map(id -> assuntoRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Assunto não encontrado com id: " + id)))
                .toList();

        return Livro.builder()
                .titulo(dto.titulo())
                .editora(dto.editora())
                .edicao(dto.edicao())
                .anoPublicacao(dto.anoPublicacao())
                .autores(autores)
                .assuntos(assuntos)
                .build();
    }

    // ---------------------------------------------------
    // Aplicar atualização em Entity existente (Update)
    // ---------------------------------------------------
    public void updateEntityFromDto(Livro livro, LivroRecordDto dto) {

        List<Autor> autores = dto.idsAutores().stream()
                .map(id -> autorRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Autor não encontrado com id: " + id)))
                .toList();

        List<Assunto> assuntos = dto.idsAssuntos().stream()
                .map(id -> assuntoRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Assunto não encontrado com id: " + id)))
                .toList();

        livro.setTitulo(dto.titulo());
        livro.setEditora(dto.editora());
        livro.setEdicao(dto.edicao());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setAutores(autores);
        livro.setAssuntos(assuntos);
    }

    // ---------------------------------------------------
    // Entity → DTO (Response)
    // ---------------------------------------------------
    public LivroRecordDto toDto(Livro livro) {

        List<AutorRecordDto> autoresDto = livro.getAutores().stream()
                .map(autor -> new AutorRecordDto(autor.getId(), autor.getNome()))
                .collect(Collectors.toList());

        List<AssuntoRecordDto> assuntosDto = livro.getAssuntos().stream()
                .map(assunto -> new AssuntoRecordDto(assunto.getDescricao()))
                .collect(Collectors.toList());

        return new LivroRecordDto(
                livro.getTitulo(),
                livro.getEditora(),
                livro.getEdicao(),
                livro.getAnoPublicacao(),
                null,                // idsAutores (entrada, não exibir na saída)
                null,                // idsAssuntos (entrada)
                livro.getId(),       // saída
                autoresDto,          // LISTA!
                assuntosDto          // LISTA!
        );
    }
}
