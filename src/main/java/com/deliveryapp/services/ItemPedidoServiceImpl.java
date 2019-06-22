package com.deliveryapp.services;

import com.deliveryapp.models.ItemPedido;
import com.deliveryapp.models.Pedido;
import com.deliveryapp.repositorys.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPedidoServiceImpl implements ItemPedidoService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PratoServiceImpl pratoServiceImpl;

    @Override
    public void salvar(Iterable<ItemPedido> itens) {

        itemPedidoRepository.saveAll(itens);
    }

    @Override
    public List<ItemPedido> listarItensPorPedido(Pedido pedido) {
        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);
        return itens;
    }

    @Override
    public ItemPedido criaItem(Long codigoPrato) {

        ItemPedido item = new ItemPedido();
        item.setPrato(pratoServiceImpl.buscarPorCodigo(codigoPrato));
        item.setQuantidade(1);
        return item;
    }

}
