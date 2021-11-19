/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rvw.itech.filexio.model.Role;
import rvw.itech.filexio.service.RoleService;

/**
 *
 * @author renj
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAll(){
        List<Role> roles = roleService.getAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
    
}
