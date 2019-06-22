package com.deliveryapp.services;

import com.deliveryapp.models.Cliente;
import com.deliveryapp.models.Endereco;

import java.util.List;

public interface EnderecoService {

    void salvar(Endereco endereco, Long codCliente, int status);

    void excluir(Long id);

    Endereco buscarPorId(Long id);

    List<Endereco> listarTodosPorCliente(Long codCliente);

    Endereco buscarPorClienteStatus(Cliente cliente, int status);

}
