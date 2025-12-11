package com.spassu.autorlivro.mapper;

import org.springframework.stereotype.Component;

import com.spassu.autorlivro.dto.AutorRecordDto;
import com.spassu.autorlivro.model.Autor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AutorMapper {

	public AutorRecordDto toDto(Autor autor) {
        return new AutorRecordDto(autor.getId(), autor.getNome());
    }
}
