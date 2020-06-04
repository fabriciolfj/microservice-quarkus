package com.github.fabriciolfj.ifood.cadastro.infra;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ConstraintViolationResponse {

    private final List<ConstraintViolationImpl> violacoes = new ArrayList<>();

    private ConstraintViolationResponse(final ConstraintViolationException exception) {
        exception.getConstraintViolations().forEach(v -> violacoes.add(ConstraintViolationImpl.of(v)));
    }

    public static ConstraintViolationResponse of(ConstraintViolationException e) {
        return new ConstraintViolationResponse(e);
    }

    public List<ConstraintViolationImpl> getViolacoes() {
        return violacoes;
    }
}
