package com.github.fabriciolfj.ifood.mp.domain.integration;

import com.github.fabriciolfj.ifood.mp.domain.model.Restaurante;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class RestauranteCadastrado {

    @Inject
    PgPool pgPool;

    @Incoming("restaurantes")
    public void receberRestaurante(String json) {
        Jsonb create = JsonbBuilder.create();
        var restaurante = create.fromJson(json, Restaurante.class);

        System.out.println(restaurante);

        restaurante.persist(pgPool);
    }
}
