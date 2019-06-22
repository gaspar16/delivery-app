package com.deliveryapp.services;

import com.deliveryapp.models.Prato;
import com.deliveryapp.repositorys.PratoRepository;
import com.deliveryapp.util.ImagemFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PratoServiceImpl implements PratoService{

    @Autowired
    private PratoRepository pratoRepository;

    @Override
    public void salvar(Prato prato, MultipartFile imagem) {

        prato.setStatus(1);
        pratoRepository.save(prato);

        String caminho = "images/" + prato.getCodigo() + ".png";
        ImagemFileUtils.salvarImagem(caminho, imagem);
    }

    @Override
    public void remover(Long codigo) {

        Prato prato = pratoRepository.getOne(codigo);

        String caminho = "images/" + prato.getCodigo() + ".png";
        ImagemFileUtils.deleteImagem(caminho);

        prato.setStatus(0);
        pratoRepository.save(prato);
    }

    @Override
    public Prato buscarPorCodigo(Long codigo) {

        return pratoRepository.getOne(codigo);
    }

    @Override
    public List<Prato> listarPratosAtivos() {

        return pratoRepository.findByStatus(1);
    }

}
