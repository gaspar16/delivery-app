package com.deliveryapp.repositorys;

import com.deliveryapp.models.Cliente;
import com.deliveryapp.models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    List<Endereco> findByCliente(Cliente cliente);
    Endereco findByClienteAndStatus(Cliente cliente, int status);
}
