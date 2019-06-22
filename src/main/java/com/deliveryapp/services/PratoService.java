package com.deliveryapp.services;

import com.deliveryapp.models.Prato;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PratoService {

    void salvar(Prato prato, MultipartFile imagem);

    void remover(Long codigo);

    Prato buscarPorCodigo(Long codigo);

    List<Prato> listarPratosAtivos();

}
