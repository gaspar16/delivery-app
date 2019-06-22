package com.deliveryapp.services;

import com.deliveryapp.models.Cliente;
import com.deliveryapp.models.Role;
import com.deliveryapp.repositorys.ClienteRepository;
import com.deliveryapp.repositorys.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void salvar(Cliente cliente) {

        cliente.setCpf(cliente.getCpf().replaceAll("\\.", "").replaceAll("\\-",""));
        cliente.setSenha(new BCryptPasswordEncoder().encode(cliente.getSenha()));

        Role role = roleRepository.findByRoleCliente("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        cliente.setRoles(roles);

        clienteRepository.save(cliente);

    }

    @Override
    public void excluir(Long codigo) {

        clienteRepository.deleteById(codigo);
    }

    @Override
    public Cliente buscaPorCodigo(Long codigo) {

        return clienteRepository.getOne(codigo);
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }


}
