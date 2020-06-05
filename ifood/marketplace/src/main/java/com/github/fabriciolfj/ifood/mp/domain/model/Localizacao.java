package com.github.fabriciolfj.ifood.mp.domain.model;

public class Localizacao {

    public Long id;

    public Double latitude;

    public Double longitude;

    @Override
    public String toString() {
        return "Localizacao{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
