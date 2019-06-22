package com.deliveryapp.services;

import com.deliveryapp.models.Cliente;
import com.deliveryapp.models.Endereco;
import com.deliveryapp.repositorys.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoServiceImpl implements EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteServiceImpl clienteServiceImpl;

    @Override
    public void salvar(Endereco endereco, Long codCliente, int status) {
        Cliente cliente = clienteServiceImpl.buscaPorCodigo(codCliente);
        endereco.setCliente(cliente);

        endereco.setStatus(status);
        enderecoRepository.save(endereco);
    }

    @Override
    public void excluir(Long id) {

        enderecoRepository.deleteById(id);
    }

    @Override
    public Endereco buscarPorId(Long id) {

        return enderecoRepository.getOne(id);
    }

    @Override
    public List<Endereco> listarTodosPorCliente(Long codCliente) {
        Cliente cliente = clienteServiceImpl.buscaPorCodigo(codCliente);
        return enderecoRepository.findByCliente(cliente);
    }

    @Override
    public Endereco buscarPorClienteStatus(Cliente cliente, int status) {
        return enderecoRepository.findByClienteAndStatus(cliente, status);
    }

}
