package com.github.fabriciolfj.ifood.mp.api.dto;


import io.vertx.mutiny.sqlclient.Row;

import java.math.BigDecimal;

public class PratoDTO {

    public Long id;

    public String nome;

    public String descricao;

    public BigDecimal preco;

    public static PratoDTO from(final Row row) {
        var dto = new PratoDTO();
        dto.descricao = row.getString("descricao");
        dto.nome = row.getString("nome");
        dto.id = row.getLong("id");
        dto.preco = row.getBigDecimal("preco");
        return dto;
    }

}
