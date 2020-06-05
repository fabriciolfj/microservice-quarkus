package com.github.fabriciolfj.ifood.mp.domain.model;

import com.github.fabriciolfj.ifood.mp.api.dto.RestauranteDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import java.util.stream.StreamSupport;

public class Restaurante {

    public Long id;

    public String nome;

    public Localizacao localizacao;

    public static Multi<RestauranteDTO> findAll(PgPool pgPool) {
        Uni<RowSet<Row>> preparedQuery = pgPool
                .preparedQuery("Select * from restaurante ");
        return getData(preparedQuery);
    }

    private static Multi<RestauranteDTO> getData(Uni<RowSet<Row>> preparedQuery) {
        return preparedQuery.onItem().produceMulti(rowSet -> Multi.createFrom().items(() ->
                StreamSupport.stream(rowSet.spliterator(), false))).onItem().apply(RestauranteDTO::fromDTO);
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", localizacao=" + localizacao +
                '}';
    }

    public void persist(PgPool pgPool) {
        pgPool.preparedQuery("insert into localizacao (id, latitude, longitude) values ($1, $2, $3)",
                Tuple.of(localizacao.id, localizacao.latitude, localizacao.longitude)).await().indefinitely();

        pgPool.preparedQuery("insert into restaurante (id, nome, localizacao_id) values ($1, $2, $3)",
                Tuple.of(id, nome, localizacao.id)).await().indefinitely();
    }
}
