package com.deliveryapp.security;

import com.deliveryapp.models.Cliente;
import com.deliveryapp.services.ClienteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteServiceImpl clienteServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Cliente cliente = clienteServiceImpl.buscarPorEmail(username);

        if(cliente == null){
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }

        return new User(cliente.getUsername(),cliente.getPassword(),true,true,true,true,cliente.getAuthorities());

    }

}
