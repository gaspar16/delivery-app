package com.deliveryapp.services;

import com.deliveryapp.models.Cliente;
import com.deliveryapp.models.Pedido;
import com.deliveryapp.repositorys.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void salvar(Pedido pedido, int status) {
        pedido.setStatus(status);
        pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedido> listarPorCliente(Cliente cliente) {

        return pedidoRepository.findByClienteOrderByDataPedidoDesc(cliente);
    }

    @Override
    public List<Pedido> listarPendentes() {

        List<Pedido> pedidos = pedidoRepository.findAll();
        List<Pedido> pedidosPendentes = new ArrayList<Pedido>();

        for(int i = 0; i < pedidos.size(); i++) {

            if(pedidos.get(i).getStatus() == 0) {
                pedidosPendentes.add(pedidos.get(i));
            }

        }

        return pedidosPendentes;
    }

    @Override
    public List<Pedido> listarEnviados() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<Pedido> pedidosEnviados = new ArrayList<Pedido>();

        for(int i = 0; i < pedidos.size(); i++) {

            if(pedidos.get(i).getStatus() == 1) {
                pedidosEnviados.add(pedidos.get(i));
            }

        }

        return pedidosEnviados;
    }

    @Override
    public Pedido buscarPorCodigo(Long codigo) {

        return pedidoRepository.getOne(codigo);
    }

    @Override
    public List<Pedido> listarTodos() {

        return pedidoRepository.findAll();
    }

}
