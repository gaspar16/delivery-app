package com.deliveryapp.controllers;

import com.deliveryapp.services.ClienteServiceImpl;
import com.deliveryapp.services.PedidoServiceImpl;
import com.deliveryapp.services.PratoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @Autowired
    private PratoServiceImpl pratoServiceImpl;

    @Autowired
    private PedidoServiceImpl pedidoServiceImpl;

    @Autowired
    private ClienteServiceImpl clienteServiceImpl;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("index");

        mv.addObject("pratos", pratoServiceImpl.listarPratosAtivos());

        return mv;
    }

    @RequestMapping(value = "/logar", method = RequestMethod.GET)
    public ModelAndView formLogin(){

        ModelAndView mv = new ModelAndView("login");
        return mv;

    }

}
