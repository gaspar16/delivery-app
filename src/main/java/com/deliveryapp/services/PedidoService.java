package com.deliveryapp.services;

import com.deliveryapp.models.Cliente;
import com.deliveryapp.models.Pedido;

import java.util.List;

public interface PedidoService {

    void salvar(Pedido pedido, int status);

    List<Pedido> listarPorCliente(Cliente cliente);

    List<Pedido> listarPendentes();

    List<Pedido> listarEnviados();

    List<Pedido> listarTodos();

    Pedido buscarPorCodigo(Long codigo);

}
