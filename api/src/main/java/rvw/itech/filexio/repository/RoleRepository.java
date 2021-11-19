/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rvw.itech.filexio.model.Role;

/**
 *
 * @author renj
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    List<String> findByNom(String nom);
}
