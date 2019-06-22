package com.deliveryapp.controllers;

import com.deliveryapp.models.*;
import com.deliveryapp.services.ClienteServiceImpl;
import com.deliveryapp.services.EnderecoServiceImpl;
import com.deliveryapp.services.ItemPedidoServiceImpl;
import com.deliveryapp.services.PedidoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteServiceImpl clienteServiceImpl;

    @Autowired
    private EnderecoServiceImpl enderecoServiceImpl;

    @Autowired
    private PedidoServiceImpl pedidoServiceImpl;

    @Autowired
    private ItemPedidoServiceImpl itemPedidoServiceImpl;

    @RequestMapping(value = "/cadCliente", method = RequestMethod.GET)
    public ModelAndView novoCliente(Cliente cliente) {
        ModelAndView mv = new ModelAndView("cliente/cad-cliente");
        return mv;
    }

    @RequestMapping(value = "/cadCliente", method = RequestMethod.POST)
    public ModelAndView salvarCliente(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {

        if(result.hasErrors()) {
            return novoCliente(cliente);
        }

        clienteServiceImpl.salvar(cliente);

        long codCli = cliente.getCodigo();
        String codigoCliente = "" + codCli;

        ModelAndView mv = new ModelAndView("redirect:/cliente/cadEndereco/" + codigoCliente);
        return mv;
    }

    @RequestMapping(value = "/cadEndereco/{codigoCliente}", method = RequestMethod.GET)
    public ModelAndView novoEndereco(Endereco endereco) {
        ModelAndView mv = new ModelAndView("cliente/cad-endereco");
        mv.addObject("ufs", Arrays.asList(UF.values()));
        return mv;
    }

    @RequestMapping(value = "/cadEndereco/{codigoCliente}", method = RequestMethod.POST)
    public ModelAndView salvarEndereco(@PathVariable("codigoCliente") long codigoCliente, @Valid Endereco endereco, BindingResult result, RedirectAttributes attributes) {

        if(result.hasErrors()) {
            return novoEndereco(endereco);
        }

        List<Endereco> enderecos = enderecoServiceImpl.listarTodosPorCliente(codigoCliente);

        if(enderecos.size() > 0) {

            for(int i = 0; i < enderecos.size();i++) {
                enderecos.get(i).setStatus(0);
            }

            enderecoServiceImpl.salvar(endereco, codigoCliente, 1);

        }else {
            enderecoServiceImpl.salvar(endereco, codigoCliente, 1);
        }

        ModelAndView mv = new ModelAndView("redirect:/cliente/enderecosCliente");

        return mv;
    }

    @RequestMapping(value = "/dadosCliente", method = RequestMethod.GET)
    public ModelAndView dadosCliente(Cliente cliente) {

        ModelAndView mv = new ModelAndView("cliente/dados-cliente");

        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails user = (UserDetails) auth;

        Cliente clienteDados = clienteServiceImpl.buscarPorEmail(user.getUsername());

        mv.addObject("cliente", clienteServiceImpl.buscaPorCodigo(clienteDados.getCodigo()));

        return mv;
    }

    @RequestMapping(value = "/dadosCliente", method = RequestMethod.POST)
    public ModelAndView AtualizarDadosCliente(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {

        if(result.hasErrors()) {
            return dadosCliente(cliente);
        }

        clienteServiceImpl.salvar(cliente);
        attributes.addFlashAttribute("mensagem", "Dados atualizados com sucesso.");
        ModelAndView mv = new ModelAndView("cliente/dados-cliente");
        return mv;
    }

    @RequestMapping(value = "/pedidosCliente", method = RequestMethod.GET)
    public ModelAndView listarPedidosDoCliente() {

        ModelAndView mv = new ModelAndView("cliente/list-pedidos");

        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails user = (UserDetails) auth;

        Cliente cliente = clienteServiceImpl.buscarPorEmail(user.getUsername());

        List<Pedido> pedidosCliente = pedidoServiceImpl.listarPorCliente(cliente);

        for(Pedido p : pedidosCliente){

            List<ItemPedido> itens = new ArrayList<>();

            itens = itemPedidoServiceImpl.listarItensPorPedido(p);
            p.setItens(itens);

        }

        mv.addObject("pedidosCliente", pedidosCliente);

        return mv;
    }

    @RequestMapping(value = "/enderecosCliente", method = RequestMethod.GET)
    public ModelAndView listarEnderecosDoCliente() {

        ModelAndView mv = new ModelAndView("cliente/list-enderecos");

        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails user = (UserDetails) auth;

        Cliente cliente = clienteServiceImpl.buscarPorEmail(user.getUsername());

        mv.addObject("enderecosCliente",enderecoServiceImpl.listarTodosPorCliente(cliente.getCodigo()));

        mv.addObject("cliente",cliente);

        return mv;
    }

}
