package com.deliveryapp.controllers;

import com.deliveryapp.models.Cliente;
import com.deliveryapp.models.Endereco;
import com.deliveryapp.models.ItemPedido;
import com.deliveryapp.models.Pedido;
import com.deliveryapp.services.ClienteServiceImpl;
import com.deliveryapp.services.EnderecoServiceImpl;
import com.deliveryapp.services.ItemPedidoServiceImpl;
import com.deliveryapp.services.PedidoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoServiceImpl pedidoServiceImpl;

    @Autowired
    private ClienteServiceImpl clienteServiceImpl;

    @Autowired
    private ItemPedidoServiceImpl itemPedidoServiceImpl;

    @Autowired
    private EnderecoServiceImpl enderecoServiceImpl;

    @RequestMapping(value = "/finalizar", method = RequestMethod.GET)
    public ModelAndView finalizar(HttpSession session) {

        ModelAndView mv = new ModelAndView("redirect:/cliente/pedidosCliente");

        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails user = (UserDetails) auth;

        Cliente cliente = clienteServiceImpl.buscarPorEmail(user.getUsername());

        List<Endereco> enderecosCliente = enderecoServiceImpl.listarTodosPorCliente(cliente.getCodigo());

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        for(Endereco endereco : enderecosCliente){
            if(endereco.getStatus() == 1){
                pedido.setEnderecoDeEntrega(endereco);
            }
        }

        pedido.setTotal(BigDecimal.valueOf(0));
        pedidoServiceImpl.salvar(pedido, 0);

        Iterable<ItemPedido> sacola = (Iterable<ItemPedido>) session.getAttribute("sacola");

        Double total = 0.0;

        for(ItemPedido item : sacola) {
            item.setPedido(pedido);
            total += item.getValor().doubleValue();
        }

        itemPedidoServiceImpl.salvar(sacola);

        total = (Double) session.getAttribute("total");

        pedido.setTotal(BigDecimal.valueOf(total));

        pedidoServiceImpl.salvar(pedido, 0);

        session.removeAttribute("sacola");
        session.removeAttribute("total");

        return mv;
    }

    @RequestMapping(value = "/pedir")
    public ModelAndView pedirProduto(){

        ModelAndView mv = new ModelAndView("sacola/endereco-entrega");
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails user = (UserDetails) auth;

        Cliente cliente = clienteServiceImpl.buscarPorEmail(user.getUsername());

        mv.addObject("enderecosCliente",enderecoServiceImpl.listarTodosPorCliente(cliente.getCodigo()));

        mv.addObject("cliente",cliente);
        return mv;

    }

    @RequestMapping(value = "/pedir", method = RequestMethod.POST)
    public ModelAndView selecionarEnderecoAtualDoCliente(@RequestParam(value="id") long id) {

        ModelAndView mv = new ModelAndView("redirect:/pedido/telaPedido");

        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails user = (UserDetails) auth;

        Cliente cliente = clienteServiceImpl.buscarPorEmail(user.getUsername());

        List<Endereco> enderecosDoCliente = enderecoServiceImpl.listarTodosPorCliente(cliente.getCodigo());

        Endereco endereco = enderecoServiceImpl.buscarPorId(id);

        for (Endereco end : enderecosDoCliente) {

            if (end.getId() == endereco.getId()) {
                enderecoServiceImpl.salvar(end, cliente.getCodigo(), 1);
            } else {
                enderecoServiceImpl.salvar(end, cliente.getCodigo(), 0);
            }
        }

        return mv;
    }

    @RequestMapping(value = "/telaPedido", method = RequestMethod.GET)
    public ModelAndView telaDoPedido(HttpSession session){

        ModelAndView mv = new ModelAndView("sacola/tela-pedido");

        session.getAttribute("sacola");

        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails user = (UserDetails) auth;

        Cliente cliente = clienteServiceImpl.buscarPorEmail(user.getUsername());

        Endereco endereco = enderecoServiceImpl.buscarPorClienteStatus(cliente,1);

        mv.addObject("endereco", endereco);

        return mv;

    }

    @RequestMapping(value = "/telaPedido", method = RequestMethod.POST)
    public ModelAndView atualizar(HttpSession session, @RequestParam(value="qtdItem") int qtdItem, @RequestParam(value="codigo") long codigo){

        ModelAndView mv = new ModelAndView("redirect:/pedido/telaPedido");

        List<ItemPedido> sacola = (List<ItemPedido>) session.getAttribute("sacola");

        int index = this.exists(codigo,sacola);

        sacola.get(index).setQuantidade(qtdItem);

        session.setAttribute("total",sumTotal(session));
        session.setAttribute("sacola", sacola);

        return mv;

    }

    @RequestMapping(value = "/remove/{codigo}", method = RequestMethod.GET)
    public String remove(@PathVariable("codigo") long codigo, HttpSession session) {

        Double total = 0.0;

        List<ItemPedido> sacola = (List<ItemPedido>) session.getAttribute("sacola");
        int index = this.exists(codigo, sacola);

        if(sacola.get(index).getQuantidade() > 1){
            int qtd = sacola.get(index).getQuantidade();
            sacola.get(index).setQuantidade(qtd-1);

        }else {
            sacola.remove(index);
        }

        for(ItemPedido item : sacola) {
            total += item.getQuantidade() * item.getPrato().getPreco().doubleValue();
        }

        session.setAttribute("sacola", sacola);
        session.setAttribute("total", total);
        return "redirect:/pedido/telaPedido";
    }

    private int exists(long codigo, List<ItemPedido> sacola) {

        for(int i = 0; i < sacola.size(); i++) {

            if(sacola.get(i).getPrato().getCodigo() == codigo) {
                return i;
            }
        }

        return -1;

    }

    private double sumTotal(HttpSession session){

        List<ItemPedido> sacola = (List<ItemPedido>) session.getAttribute("sacola");
        double s = 0;

        for(ItemPedido item : sacola){
            s += item.getQuantidade() * item.getPrato().getPreco().doubleValue();
        }

        return s;

    }

}
