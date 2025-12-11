package com.spassu.autorlivro.dto;

import com.fasterxml.jackson.annotation.JsonView;

public record AssuntoRecordDto(

        @JsonView({AssuntoView.Create.class, AssuntoView.Update.class, AssuntoView.Response.class})
        String descricao

) {
    public interface AssuntoView {
        interface Create {}
        interface Update {}
        interface Response {}
    }
}
