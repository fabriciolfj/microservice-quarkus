package com.github.fabriciolfj.ifood.cadastro.api.mapper;

import com.github.fabriciolfj.ifood.cadastro.domain.model.Prato;
import com.github.fabriciolfj.ifood.cadastro.api.dto.PratoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface PratoMapper {

    PratoDTO toDto(final Prato prato);

    @Mapping(target = "preco", source = "preco")
    void toPrato(final PratoDTO dto, @MappingTarget Prato prato);

    Prato toDomain(final PratoDTO dto);
}
