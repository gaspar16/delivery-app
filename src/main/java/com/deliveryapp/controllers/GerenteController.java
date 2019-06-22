package com.deliveryapp.controllers;

import com.deliveryapp.models.Pedido;
import com.deliveryapp.models.Prato;
import com.deliveryapp.services.PedidoServiceImpl;
import com.deliveryapp.services.PratoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/gerente")
public class GerenteController {

    @Autowired
    private PratoServiceImpl pratoServiceImpl;

    @Autowired
    private PedidoServiceImpl pedidoServiceImpl;

    @RequestMapping(value = "/addPrato", method = RequestMethod.GET)
    public ModelAndView addPrato(Prato prato) {
        ModelAndView mv = new ModelAndView("gerente/add-prato");
        return mv;
    }

    @RequestMapping(value = "/addPrato", method = RequestMethod.POST)
    public ModelAndView salvarPrato(@Valid Prato prato, BindingResult result, RedirectAttributes attributes, @RequestParam(value="imagem") MultipartFile imagem) {

        ModelAndView mv = new ModelAndView("redirect:/gerente/addPrato");

        if(result.hasErrors()) {
            return addPrato(prato);
        }

        pratoServiceImpl.salvar(prato, imagem);
        attributes.addFlashAttribute( "mensagem", "Prato adicionado com sucesso!");

        return mv;
    }

    @RequestMapping(value = "/listarPratos", method = RequestMethod.GET)
    public ModelAndView listarPratos() {

        ModelAndView mv = new ModelAndView("gerente/list-prato");

        mv.addObject("pratos", pratoServiceImpl.listarPratosAtivos());

        return mv;
    }

    @RequestMapping(value = "/editPrato/{codigo}", method = RequestMethod.GET)
    public ModelAndView editarPrato(@PathVariable("codigo") long codigo) {

        ModelAndView mv = new ModelAndView("gerente/add-prato");

        Prato prato = pratoServiceImpl.buscarPorCodigo(codigo);
        mv.addObject(prato);

        return mv;

    }

    @RequestMapping(value = "/editPrato/{codigo}", method = RequestMethod.POST)
    public ModelAndView atualizarPrato(@PathVariable("codigo") long codigo, @Valid Prato prato, BindingResult result, RedirectAttributes attributes, @RequestParam(value = "imagem") MultipartFile imagem) {

        if(result.hasErrors()) {
            return addPrato(prato);
        }

        pratoServiceImpl.salvar(prato, imagem);

        ModelAndView mv = new ModelAndView("redirect:/gerente/listarPratos");

        return mv;

    }

    @RequestMapping(value = "/excluirPrato/{codigo}", method = RequestMethod.GET)
    public String excluirPrato(@PathVariable("codigo") long codigo) {

        pratoServiceImpl.remover(codigo);

        return "redirect:/gerente/listarPratos";

    }

    @RequestMapping(value = "/listarPedidosPendentes", method = RequestMethod.GET)
    public ModelAndView listarPedidosPendentes() {

        ModelAndView mv = new ModelAndView("gerente/list-pedidos-pendentes");

        mv.addObject("pedidosPendentes", pedidoServiceImpl.listarPendentes());

        return mv;
    }

    @RequestMapping(value = "/listarPedidosEnviados", method = RequestMethod.GET)
    public ModelAndView listarPedidosEnviados() {

        ModelAndView mv = new ModelAndView("gerente/list-pedidos-enviados");

        mv.addObject("pedidosEnviados", pedidoServiceImpl.listarEnviados());

        return mv;
    }

    @RequestMapping(value = "/enviarPedido/{codigo}", method = RequestMethod.GET)
    public String enviarPedido(@PathVariable long codigo){

        Pedido pedido = pedidoServiceImpl.buscarPorCodigo(codigo);
        pedidoServiceImpl.salvar(pedido,1);

        return "redirect:/gerente/listarPedidosEnviados";

    }

}
