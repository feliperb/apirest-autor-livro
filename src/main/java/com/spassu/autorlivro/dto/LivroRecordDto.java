package com.spassu.autorlivro.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

public record LivroRecordDto(

        // ---------- CAMPOS DE ENTRADA ----------
        @JsonView({BookView.Create.class, BookView.Update.class})
        String titulo,

        @JsonView({BookView.Create.class, BookView.Update.class})
        String editora,

        @JsonView({BookView.Create.class, BookView.Update.class})
        Integer edicao,

        @JsonView({BookView.Create.class, BookView.Update.class})
        String anoPublicacao,

        @JsonView({BookView.Create.class, BookView.Update.class})
        List<Long> idsAutores,

        @JsonView({BookView.Create.class, BookView.Update.class})
        List<Long> idsAssuntos,


        // ---------- CAMPOS DE SAÍDA ----------
        @JsonView(BookView.Response.class)
        Long id,

        @JsonView(BookView.Response.class)
        List<AutorRecordDto> autores,

        @JsonView(BookView.Response.class)
        List<AssuntoRecordDto> assuntos

) {
    public interface BookView {
        interface Create {}
        interface Update {}
        interface Response {}
    }
}
