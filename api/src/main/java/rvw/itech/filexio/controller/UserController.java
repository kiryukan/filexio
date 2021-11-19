package rvw.itech.filexio.controller;


import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rvw.itech.filexio.model.User;
import rvw.itech.filexio.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import rvw.itech.filexio.model.File;
import rvw.itech.filexio.model.JwtRequest;
import rvw.itech.filexio.model.JwtResponse;
import rvw.itech.filexio.model.Role;
import rvw.itech.filexio.service.FileService;
import rvw.itech.filexio.security.JWTUtility;
import rvw.itech.filexio.service.RoleService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author renj
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtility jwtTokenUtil;
    
    /*@GetMapping("/")
    @CrossOrigin(origins="*")
    public String test(){
        return "This is homepage!";
    }
    
    @GetMapping("/test2")
    @CrossOrigin(origins="*")
    public String testAuth(){
        return "This is authenticated test page!";
    }*/
    
    @GetMapping("/get/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping("/get/{id}")
    @CrossOrigin
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
        User user = userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @GetMapping("/get/username")
    @CrossOrigin
    public ResponseEntity<User> getCurrentUser(Authentication authentication){
        User user = userService.getUserByUsername(authentication.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @GetMapping("/get/by-email/{email}")
    @CrossOrigin
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email){
        User user = userService.getUserByUsername(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    /*@PostMapping("/authenticate")
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(), 
                        jwtRequest.getPassword())
            );
        }catch(BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        System.out.println("request username " + jwtRequest.getUsername());
        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        final String jwtToken = jwtTokenUtil.generateToken(userDetails);
        
        return new JwtResponse(jwtToken);
    }*/
    
    
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User u){
        if(this.userService.userUsernameNotAlreadyExist(u.getUsername())){
            if(this.userService.userEmailNotAlreadyExist(u.getEmail())){
                u.setAvailableDiskSpace(100000L);
                Role r = roleService.getById(3L);
                u.setRole(r);
                User user = userService.addUser(u);
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }
            else{
                // RETURN MAP AS JSON STRUCT
                HashMap<String, String> map = new HashMap();
                map.put("error", "email");
                return new ResponseEntity<>(map,HttpStatus.CONFLICT);
            }
        }
        else{
                HashMap<String, String> map = new HashMap();
                map.put("error", "username");
                return new ResponseEntity<>(map,HttpStatus.CONFLICT);
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User u){
        User user = userService.updateUser(u);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/{userId}/files")
    @CrossOrigin
    public ResponseEntity<List<File>> getAllUserFiles(@PathVariable("userId") Long id){
        List<File> files = this.fileService.getFilesFromUserId(id);
        for(File file:files){
            if(file.getIsRoot()){
                this.fileService.setRecursiveFolderSize(file.getId()); // VERIFY ALL FOLDERS SIZES
            }
        }
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
    
    @GetMapping("/{userId}/{folderId}/files")
    @CrossOrigin
    public ResponseEntity<List<File>> getUserFilesFromFolder(@PathVariable("userId") Long id, @PathVariable("folderId") Long folderId){
        this.fileService.setRecursiveFolderSize(folderId); // VERIFY ALL FOLDERS SIZES
        List<File> files = this.fileService.findByParentFolder(folderId);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
}
