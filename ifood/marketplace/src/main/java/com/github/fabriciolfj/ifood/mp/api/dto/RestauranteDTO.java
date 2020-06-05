package com.github.fabriciolfj.ifood.mp.api.dto;

import io.vertx.mutiny.sqlclient.Row;

public class RestauranteDTO {

    public Long id;
    public String nome;

    public static RestauranteDTO fromDTO(final Row row) {
        var dto = new RestauranteDTO();
        dto.id = row .getLong(("id"));
        dto.nome = row.getString("nome");
        return dto;
    }
}
