/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rvw.itech.filexio.model.Role;
import rvw.itech.filexio.repository.RoleRepository;

/**
 *
 * @author renj
 */
@Service
public class RoleService{
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role addRole(Role role){
        return roleRepository.save(role);
    }

    public Role updateRole(Role role){
        return roleRepository.save(role);
    }
    
    public Role getById(Long id){
        return roleRepository.getById(id);
    }
    
    public void deleteById(Long id){
        roleRepository.deleteById(id);
    }
    
    public List<Role> getAll(){
        return roleRepository.findAll();
    }
}
