/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Value;
import rvw.itech.filexio.utility.UtilityIOService;
import rvw.itech.filexio.utility.UtilityService;

/**
 *
 * @author renj
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Initialize JsonIgnore after serialization
public class User implements Serializable/*, UserDetails*/{
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String rootFolderName;
    private Long usedDiskSpace;
    private Long availableDiskSpace;
    
    
    @ManyToOne //@JsonIgnore
    @JoinColumn(name="role_id")
    private Role role;
    
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDate lastConnection;
    
    @OneToMany(/*fetch = FetchType.EAGER, */cascade=CascadeType.ALL, mappedBy="user")
    @JsonIgnore
    private List<File> files = new ArrayList<>();

    
    /*** CONSTRUCTORS ***/
    public User(){}
    
    public User(String username, String email, String password, Role role, Long availableDiskSpace, Long usedDiskSpace, LocalDate lastConnection) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.lastConnection = lastConnection;
        //this.createFolder("");
        this.availableDiskSpace = availableDiskSpace;
        this.usedDiskSpace = usedDiskSpace;
    }

    public User(User user){
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.lastConnection = user.getLastConnection();
        this.rootFolderName = user.getRootFolderName();
        this.availableDiskSpace = user.availableDiskSpace;
        this.usedDiskSpace = user.getUsedDiskSpace();
        this.files = user.getFiles();
    }
    
    /*** GETTERS ***/

    public Long getId(){
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRootFolderName() {
        return rootFolderName;
    }

    public Long getUsedDiskSpace() {
        return usedDiskSpace;
    }

    public Role getRole() {
        return role;
    }

    public LocalDate getLastConnection() {
        return lastConnection;
    }

    public List<File> getFiles() {
        return files;
    }

    public Long getAvailableDiskSpace() {
        return availableDiskSpace;
    }

    
    
    /*** SETTERS ***/
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRootFolderName(String rootFolderName) {
        this.rootFolderName = rootFolderName;
    }

    public void setUsedDiskSpace(Long usedDiskSpace) {
        this.usedDiskSpace = usedDiskSpace;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setLastConnection(LocalDate lastConnection) {
        this.lastConnection = lastConnection;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
    

    public void setAvailableDiskSpace(Long availableDiskSpace) {
        this.availableDiskSpace = availableDiskSpace;
    }
    
    
    
    /*** USER ROOTFOLDER CREATING METHOD ***/

    /*private String createUserFolderName() {
        String appendice = "0123456789ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
        return UtilityService.generateRootFolderName(8, new SecureRandom(), appendice);
    }
    
    
    public void createFolder(String baseUserPath){
        String append = this.createUserFolderName();
        this.rootFolderName = this.username + "-" + append;
        System.out.println("root folder name: "+this.rootFolderName);
        UtilityIOService utilityIOService = new UtilityIOService();
        utilityIOService.createRootFolder(this.rootFolderName);
    }*/

    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public boolean isAccountNonExpired() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }

    @Override
    public boolean isEnabled() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }*/

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", rootFolderName=" + rootFolderName + ", usedDiskSpace=" + usedDiskSpace + ", availableDiskSpace=" + availableDiskSpace + ", role=" + role + ", lastConnection=" + lastConnection + ", files=" + files + '}';
    }

    
}
