/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import rvw.itech.filexio.exception.UserNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rvw.itech.filexio.model.User;
import rvw.itech.filexio.repository.UserRepository;
import rvw.itech.filexio.security.PassCryptoService;
import rvw.itech.filexio.repository.RoleRepository;

/**
 *
 * @author renj
 */
@Service
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;        
    }
    
    public User addUser(User user){
        String pass = user.getPassword();
        PassCryptoService passCryptS = new PassCryptoService();
        user.setPassword(passCryptS.encodePassword(pass));
        user.createFolder("");
        user.setLastConnection(LocalDate.now());
        return userRepository.save(user);
    }
    
    public User getUser(Long id){
        //return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
        return userRepository.getById(id);
    }
    
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    
    public User updateUser(User user){
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
    
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        User user = userRepository.findByUsername(username);
            if(user == null){
                throw new UsernameNotFoundException("User not found");
            }
        return user;
    }
    
    public User getUserByEmail(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        User user = userRepository.findUserWithEmail(username);
            if(user == null){
                throw new UsernameNotFoundException("User not found");
            }
        return user;
    }
    
    public User getUserByCredentials(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        User user = userRepository.findByUsername(username);
            if(user == null){
                throw new UsernameNotFoundException("User not found");
            }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        User user = userRepository.findByUsername(username);
        System.out.println(user.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        
        System.out.println("userdetails: " + userDetails.getUsername() + " " + userDetails.getPassword());
        //return new org.springframework.security.core.userdetails.User("admin", "password", new ArrayList<>());
        return userDetails;
    }
    
    public boolean userUsernameNotAlreadyExist(String username){
        return this.userRepository.findUserWithUsername(username) == null;
    }
    
        public boolean userEmailNotAlreadyExist(String email){
        return this.userRepository.findUserWithEmail(email) == null;
    }
}
