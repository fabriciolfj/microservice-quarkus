package com.github.fabriciolfj.ifood.pedido.model.integration;

import com.github.fabriciolfj.ifood.pedido.api.dto.PedidoRealizadoDTO;
import com.github.fabriciolfj.ifood.pedido.api.dto.PratoPedidoDTO;
import com.github.fabriciolfj.ifood.pedido.model.domain.Pedido;
import com.github.fabriciolfj.ifood.pedido.model.domain.Prato;
import com.github.fabriciolfj.ifood.pedido.model.domain.Restaurante;
import org.bson.types.Decimal128;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.JsonbBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;

@ApplicationScoped
public class PedidoRealizadoIncoming {

    @Incoming("pedidos")
    public void lerPedidos(PedidoRealizadoDTO dto) {
        System.out.println("-----------------");
        System.out.println(dto);

        Pedido p = new Pedido();
        p.cliente = dto.cliente;
        p.pratos = new ArrayList<>();
        dto.pratos.forEach(prato -> p.pratos.add(from(prato)));
        Restaurante restaurante = new Restaurante();
        restaurante.nome = dto.restaurante.nome;
        p.restaurante = restaurante;
        String json = JsonbBuilder.create().toJson(dto);
        p.persist();
    }

    private Prato from(PratoPedidoDTO prato) {
        Prato p = new Prato();
        p.descricao = prato.descricao;
        p.nome = prato.nome;
        p.preco = new Decimal128(prato.preco == null ? new BigDecimal("0") : prato.preco);
        return p;
    }
}
