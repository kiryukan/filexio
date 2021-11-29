/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author renj
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Initialize JsonIgnore after serialization
public class User implements Serializable/*, UserDetails*/{
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //@Column(name="username")
    private String username;
    //@Column(name="email")
    private String email;
    //@Column(name="password")
    private String password;
    //@Column(name="root_folder_name")
    private String rootFolderName;
    //@Column(name="used_disk_space")
    private Long usedDiskSpace;
    //@Column(name="available_disk_space")
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

    public void addFile(File file){
        this.files.add(file);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", rootFolderName=" + rootFolderName + ", usedDiskSpace=" + usedDiskSpace + ", availableDiskSpace=" + availableDiskSpace + ", role=" + role + ", lastConnection=" + lastConnection + ", files=" + files + '}';
    }    
}
