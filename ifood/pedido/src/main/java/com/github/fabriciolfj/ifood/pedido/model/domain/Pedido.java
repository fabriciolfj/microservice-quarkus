package com.github.fabriciolfj.ifood.pedido.model.domain;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.util.List;

@MongoEntity(collection = "pedidos", database = "pedido")
public class Pedido extends PanacheMongoEntity {

    public String cliente;

    public List<Prato> pratos;

    public String entregador;

    public Restaurante restaurante;

    public Localizacao localizacao;
}
