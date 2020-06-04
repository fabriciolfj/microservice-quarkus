package com.github.fabriciolfj.ifood.cadastro.api.mapper;

import com.github.fabriciolfj.ifood.cadastro.domain.model.Restaurante;
import com.github.fabriciolfj.ifood.cadastro.api.dto.RestauranteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    @Mapping(target = "nome", source = "nomeFantasia")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "localizacao.id", ignore = true)
    Restaurante toDomain(final RestauranteDTO dto);

    @Mapping(target = "nome", source = "nomeFantasia")
    void toRestaurante(final RestauranteDTO dto, @MappingTarget Restaurante restaurante);

    @Mapping(target = "nomeFantasia", source = "nome")
    //@Mapping(target = "dataCriacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
    RestauranteDTO toDTO(final Restaurante restaurante)
;}
