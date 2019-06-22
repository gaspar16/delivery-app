package com.deliveryapp.repositorys;

import com.deliveryapp.models.Prato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PratoRepository extends JpaRepository<Prato, Long> {

    List<Prato> findByStatus(int status);
}
