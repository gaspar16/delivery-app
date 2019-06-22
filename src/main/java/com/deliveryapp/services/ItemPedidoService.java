package com.deliveryapp.services;

import com.deliveryapp.models.ItemPedido;
import com.deliveryapp.models.Pedido;

import java.util.List;

public interface ItemPedidoService {

    ItemPedido criaItem(Long codigoPrato);

    void salvar(Iterable<ItemPedido> itens);

    List<ItemPedido> listarItensPorPedido(Pedido pedido);

}
