package com.github.fabriciolfj.ifood.cadastro.api.dto;

import com.github.fabriciolfj.ifood.cadastro.domain.model.Restaurante;
import com.github.fabriciolfj.ifood.cadastro.infra.DTO;
import com.github.fabriciolfj.ifood.cadastro.infra.ValidDTO;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ValidDTO
public class RestauranteDTO implements DTO {

    @NotEmpty
    @NotNull
    public String proprietario;
    @CNPJ
    public String cnpj;
    @Size(min = 3, max = 30)
    public String nomeFantasia;
    public LocalizacaoDTO localizacao;

    @Override
    public boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        if (Restaurante.find("cnpj", cnpj).count() > 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("CNPJ já cadastrado")
                    .addPropertyNode("cnpj")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
