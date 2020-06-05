package com.github.fabriciolfj.ifood.mp.api;

import com.github.fabriciolfj.ifood.mp.api.dto.PratoDTO;
import com.github.fabriciolfj.ifood.mp.api.dto.RestauranteDTO;
import com.github.fabriciolfj.ifood.mp.domain.model.Prato;
import com.github.fabriciolfj.ifood.mp.domain.model.Restaurante;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    @Inject
    PgPool pgPool;

    @GET
    public Multi<RestauranteDTO> findAll() {
        return Restaurante.findAll(pgPool);
    }

    @GET
    @Path("/{restauranteId}/pratos")
    public Multi<PratoDTO> findPratos(@PathParam("restauranteId") final Long id) {
        return Prato.findByRestaurante(id, pgPool);
    }
}
