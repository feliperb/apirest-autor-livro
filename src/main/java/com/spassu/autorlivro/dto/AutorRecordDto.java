package com.spassu.autorlivro.dto;

import com.fasterxml.jackson.annotation.JsonView;

public record AutorRecordDto(
		
		@JsonView({AutorView.Create.class, AutorView.Update.class, AutorView.Response.class})
        Long id,

        @JsonView({AutorView.Create.class, AutorView.Update.class, AutorView.Response.class})
        String nome

) {
    public interface AutorView {
        interface Create {}
        interface Update {}
        interface Response {}
    }
}
