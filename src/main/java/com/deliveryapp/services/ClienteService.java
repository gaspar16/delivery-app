package com.deliveryapp.services;

import com.deliveryapp.models.Cliente;

public interface ClienteService {

    void salvar(Cliente cliente);

    void excluir(Long codigo);

    Cliente buscaPorCodigo(Long codigo);

    Cliente buscarPorEmail(String email);

}
