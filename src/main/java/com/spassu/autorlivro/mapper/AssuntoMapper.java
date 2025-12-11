package com.spassu.autorlivro.mapper;

import org.springframework.stereotype.Component;

import com.spassu.autorlivro.dto.AssuntoRecordDto;
import com.spassu.autorlivro.model.Assunto;

@Component
public class AssuntoMapper {

    public Assunto toEntity(AssuntoRecordDto dto) {
        if (dto == null) return null;

        Assunto assunto = new Assunto();
        assunto.setDescricao(dto.descricao());
        return assunto;
    }

    public void updateEntityFromDto(Assunto assunto, AssuntoRecordDto dto) {
        if (dto.descricao() != null) {
            assunto.setDescricao(dto.descricao());
        }
    }
    
    public AssuntoRecordDto toDto(Assunto entity) {
        if (entity == null) return null;

        return new AssuntoRecordDto(
                entity.getId(),
                entity.getDescricao()
        );
    }
}
