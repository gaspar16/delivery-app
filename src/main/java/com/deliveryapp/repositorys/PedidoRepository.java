package com.deliveryapp.repositorys;

import com.deliveryapp.models.Cliente;
import com.deliveryapp.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteOrderByDataPedidoDesc(Cliente cliente);

}
