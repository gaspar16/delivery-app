package com.deliveryapp.repositorys;

import com.deliveryapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByRoleCliente(String roleCliente);
}
