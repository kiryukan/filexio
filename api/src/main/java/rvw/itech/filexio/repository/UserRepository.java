/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rvw.itech.filexio.model.User;

/**
 *
 * @author renj
 */
public interface UserRepository extends JpaRepository<User, Long>{

    @Query(value="SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);
    
    @Query(value="SELECT u FROM User u WHERE u.username = ?1")
    User findUserWithUsername(String username);
    
    @Query(value="SELECT u FROM User u WHERE u.email = ?1")
    User findUserWithEmail(String email);
    
    @Query(value="SELECT u FROM User u WHERE u.username = ?1 OR u.email = ?1")
    User findUserByCredentials(String usernameOrEmail);
    
    @Query(value="SELECT u FROM User u WHERE u.username = ?1 AND u.password = ?2")
    User getUserByUsernameAndPassword(String username, String password);

}
