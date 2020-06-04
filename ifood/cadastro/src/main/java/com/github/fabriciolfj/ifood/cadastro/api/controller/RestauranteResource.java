package com.github.fabriciolfj.ifood.cadastro.api.controller;

import com.github.fabriciolfj.ifood.cadastro.domain.model.Prato;
import com.github.fabriciolfj.ifood.cadastro.domain.model.Restaurante;
import com.github.fabriciolfj.ifood.cadastro.api.dto.PratoDTO;
import com.github.fabriciolfj.ifood.cadastro.api.dto.RestauranteDTO;
import com.github.fabriciolfj.ifood.cadastro.api.mapper.PratoMapper;
import com.github.fabriciolfj.ifood.cadastro.api.mapper.RestauranteMapper;
import com.github.fabriciolfj.ifood.cadastro.infra.ConstraintViolationResponse;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.SimplyTimed;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")
@RolesAllowed("proprietario")
@SecurityScheme(securitySchemeName = "ifood-oauth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "http://localhost:8180/auth/realms/ifood/protocol/openid-connect/token")))
@SecurityRequirement(name = "ifood-oauth")
public class RestauranteResource {

    @Inject
    private RestauranteMapper mapper;

    @Inject
    private PratoMapper pratoMapper;

    @GET
    @Counted(name = "Quantidade buscas Restaurante") //quantidade que esse método foi chamado
    @SimplyTimed(name = "Tempo simples de busca") //tempo que demoro a média de execuçãoes
    @Timed(name = "Tempo completo de busca")// disponibiliza varios agrupamentos de tempo
    //@Gauge() pegar uma informação específica, como espaço em disco, temperatura do cpu
    public List<RestauranteDTO> getAll() {
        Stream<Restaurante> restaurantes= Restaurante.streamAll();
        return restaurantes.map(r -> mapper.toDTO(r))
                .collect(Collectors.toList());
    }

    @POST
    @Transactional
    @APIResponse(responseCode = "201", description = "Caso o restaurante seja cadastrado com sucesso")
    @APIResponse(responseCode = "400", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
    public Response adicionar(@Valid RestauranteDTO dto) {
        var restaurante =  mapper.toDomain(dto);
        restaurante.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, RestauranteDTO dto) {
        Optional<Restaurante> restaurante = Restaurante.findByIdOptional(id);

        if(restaurante.isEmpty()) {
            new NotFoundException();
        }
        var entity = restaurante.get();
        mapper.toRestaurante(dto, entity);
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
    public List<PratoDTO> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> entity = getRestaurante(idRestaurante);

        Stream<Prato> pratos = Prato.stream("restaurante", entity.get());
        return pratos.map(r -> pratoMapper.toDto(r)).collect(Collectors.toList());
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    @Transactional
    public Response adicionar(@PathParam("idRestaurante") Long idRestaurante, PratoDTO dto) {
        Optional<Restaurante> entity = getRestaurante(idRestaurante);

        var prato = pratoMapper.toDomain(dto);
        prato.restaurante = entity.get();
        prato.persist();

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Tag(name = "prato")
    @Transactional
    public void atualizar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, PratoDTO dto) {
        Optional<Restaurante> restaurante = getRestaurante(idRestaurante);
        List<Prato> pratos = Prato.list("restaurante", restaurante.get());

        if(pratos.isEmpty()) {
            throw new NotFoundException();
        }

        pratos.stream()
                .filter(entity -> entity.id.equals(id))
                .map(p -> {
                    pratoMapper.toPrato(dto, p);
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
