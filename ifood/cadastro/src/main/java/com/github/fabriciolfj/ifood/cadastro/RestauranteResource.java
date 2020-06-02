package com.github.fabriciolfj.ifood.cadastro;

import com.github.fabriciolfj.ifood.cadastro.domain.model.Prato;
import com.github.fabriciolfj.ifood.cadastro.domain.model.Restaurante;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")
public class RestauranteResource {

    @GET
    public List<Restaurante> getAll() {
        return Restaurante.listAll();
    }

    @POST
    @Transactional
    public Response adicionar(Restaurante dto) {
        dto.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, Restaurante dto) {
        Optional<Restaurante> restaurante = Restaurante.findByIdOptional(id);

        if(restaurante.isEmpty()) {
            new NotFoundException();
        }
        var entity = restaurante.get();
        entity.nome = dto.nome;
        entity.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Optional<Restaurante> restaurante = Restaurante.findByIdOptional(id);
        restaurante.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
    }

    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    public List<Prato> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> entity = getRestaurante(idRestaurante);

        return Prato.list("restaurante", entity.get());
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    @Transactional
    public Response adicionar(@PathParam("idRestaurante") Long idRestaurante, Prato dto) {
        Optional<Restaurante> entity = getRestaurante(idRestaurante);

        Prato prato = new Prato();
        prato.preco = dto.preco;
        prato.nome = dto.nome;
        prato.descricao = dto.descricao;
        prato.restaurante = entity.get();
        prato.persist();

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Tag(name = "prato")
    @Transactional
    public void atualizar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, Prato prato) {
        Optional<Restaurante> restaurante = getRestaurante(idRestaurante);
        List<Prato> pratos = Prato.list("restaurante", restaurante.get());

        if(pratos.isEmpty()) {
            throw new NotFoundException();
        }

        pratos.stream()
                .filter(entity -> entity.id.equals(id))
                .map(p -> {
                    p.preco = prato.preco;
                    p.persist();
                    return true;
                })
        .findAny().ifPresentOrElse(Response::accepted, () -> {throw new NotFoundException();});
    }

    @DELETE
    @Path("{idRestaurante}/pratos/{id}")
    @Tag(name = "prato")
    @Transactional
    public void deletar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
        Optional<Restaurante> restaurante = getRestaurante(idRestaurante);
        List<Prato> pratos = Prato.list("restaurante", restaurante.get());

        pratos.stream()
                .filter(entity -> entity.id.equals(id))
                .map(p -> {
                    p.delete();
                    return true;
                })
                .findAny().ifPresentOrElse(Response::accepted, () -> {throw new NotFoundException();});

    }

    private Optional<Restaurante> getRestaurante(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> entity = Restaurante.findByIdOptional(idRestaurante);

        if (entity.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }
        return entity;
    }
}
