/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import rvw.itech.filexio.model.User;
import rvw.itech.filexio.repository.UserRepository;
import rvw.itech.filexio.security.PassCryptoService;
import rvw.itech.filexio.repository.RoleRepository;
import rvw.itech.filexio.utility.UtilityIOService;
import rvw.itech.filexio.utility.UtilityService;

/**
 *
 * @author renj
 */
@Service
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UtilityIOService utilityIOService;
    private final UtilityService utilityService;
    //private final FileService fileService;
    @Value("${files.base-directory}")
    private String basePath;

    @Autowired
    public UserService(
            UserRepository userRepository, 
            RoleRepository roleRepository, 
            UtilityIOService utilityIOService,
            UtilityService utilityService/*,
            FileService fileService*/) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.utilityIOService = utilityIOService;
        this.utilityService = utilityService;
    }
    
    public User addUser(User user){
        String rootUserFolderPath = this.createUserRootFolderPath(user);
        this.utilityIOService.createRootFolder(this.basePath + rootUserFolderPath);
        user.setRootFolderName(rootUserFolderPath);
        String pass = user.getPassword();
        PassCryptoService passCryptS = new PassCryptoService();
        user.setPassword(passCryptS.encodePassword(pass));
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
        User userMod = this.userRepository.getById(user.getId());
        String pass = user.getPassword();
        PassCryptoService passCryptS = new PassCryptoService();
        userMod.setPassword(passCryptS.encodePassword(pass));
        return userRepository.save(userMod);
    }
    
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
    
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        User user = userRepository.findUserWithUsername(username);
            if(user == null){
                throw new UsernameNotFoundException("User not found");
            }
        return user;
    }
    
    public UserDetails getUserByEmail(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserWithEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        System.out.println(user.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());        
        System.out.println("userdetails: " + userDetails.getUsername() + " " + userDetails.getPassword());
        return userDetails;
    }
    
    public UserDetails findUserByCredentials(String usernameOrEmail) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByCredentials(usernameOrEmail);
        System.out.println(("User " + usernameOrEmail + " not found"));
        if(user == null){
            throw new UsernameNotFoundException("User " + usernameOrEmail + " not found");
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());        
        System.out.println("userdetails: " + userDetails.getUsername() + " " + usernameOrEmail + " " + userDetails.getPassword());
        return userDetails;
    }
    
    public User getUserByUsernameAndPassword(String username, String password){
        User foundUser = this.userRepository.findByUsername(username);
        System.out.println("password clear / hashed: "+password+" - "+foundUser.getPassword());
        if(!BCrypt.checkpw(password, foundUser.getPassword())){
            foundUser = null;
        }
        
        return foundUser;
    }
    
    public boolean userUsernameNotAlreadyExist(String username){
        return this.userRepository.findUserWithUsername(username) == null;
    }
    
    public boolean userEmailNotAlreadyExist(String email){
        return this.userRepository.findUserWithEmail(email) == null;
    }
    
    private String createUserFolderName() {
        String appendice = "0123456789ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
        return UtilityService.generateRootFolderName(8, new SecureRandom(), appendice);
    }
    
    private String createUserRootFolderPath(User user){
        // Create root path and folder
        String appendName = this.createUserFolderName();
        String rootFolderUserPath = user.getUsername() + "-" + appendName;
        return rootFolderUserPath;
    }
}
