package com.deliveryapp.repositorys;

import com.deliveryapp.models.ItemPedido;
import com.deliveryapp.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedido(Pedido pedido);

}
