package com.github.fabriciolfj.ifood.mp.domain.model;

import com.github.fabriciolfj.ifood.mp.api.dto.PratoDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

import java.math.BigDecimal;
import java.util.stream.StreamSupport;

public class Prato {

    public Long id;

    public String nome;

    public String descricao;

    public Restaurante restaurante;

    public BigDecimal preco;

    public static Multi<PratoDTO> findAll(PgPool pgPool) {
        Uni<RowSet<Row>> preparedQuery = pgPool
                .preparedQuery("Select * from prato ");
        return getPratoDTOMulti(preparedQuery);
    }

    public static Multi<PratoDTO> findByRestaurante(Long id, PgPool pgPool) {
        Uni<RowSet<Row>> preparedQuery = pgPool.preparedQuery("Select * from prato where restaurante_id = " + id);
        return getPratoDTOMulti(preparedQuery);
    }

    private static Multi<PratoDTO> getPratoDTOMulti(Uni<RowSet<Row>> preparedQuery) {
        return preparedQuery.onItem().produceMulti(rowSet -> Multi.createFrom().items(() ->
                StreamSupport.stream(rowSet.spliterator(), false))).onItem().apply(PratoDTO::from);
    }
}
