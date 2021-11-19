/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author renj
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // avoid to serialize lazy property
public class File implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;
    private String name;
    private Long fileSize;
    private String path;
    private String sharedUri;
    
    @Temporal(TemporalType.DATE)
    private Date validitySharing;
    
    @ManyToOne(fetch = FetchType.LAZY) //@JsonIgnore
    @JoinColumn(name="file_type_id", nullable=false)
    private FileType fileType;
    private String ext;
    
    @ManyToOne(fetch = FetchType.LAZY) //@JsonIgnore
    @JsonIgnoreProperties({"user"})
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY) //@JsonIgnore
    @JsonIgnoreProperties({"parentFolder"})
    @JoinColumn(name="parent_folder_id")
    private File parentFolder;
    private boolean isRoot;
    
    @OneToMany(mappedBy = "parentFolder", cascade=CascadeType.ALL) @JsonIgnore
    //@JsonManagedReference
    private List<File> subFiles = new ArrayList();

    public File(){}

    public File(String name, Long fileSize, String path, FileType fileType, String ext, User user, File parentFolder, boolean isRoot, List<File> subFiles) {
        this.name = name;
        this.fileSize = fileSize;
        this.path = path;
        this.fileType = fileType;
        this.ext = ext;
        this.user = user;
        this.parentFolder = parentFolder;
        this.isRoot = isRoot;
        this.subFiles = subFiles;
    }
    
    public File(File file){
            this.name = file.getName();
            this.ext = file.getExt();
            this.fileType = file.getFileType();
            this.isRoot = file.getIsRoot();
            this.fileSize = file.getFileSize();
            this.user = file.getUser();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getPath() {
        return path;
    }

    public String getSharedUri() {
        return sharedUri;
    }

    public Date getValiditySharing() {
        return validitySharing;
    }

    public FileType getFileType() {
        return fileType;
    }

    public String getExt() {
        return ext;
    }

    public User getUser() {
        return user;
    }

    public File getParentFolder() {
        return parentFolder;
    }

    public boolean getIsRoot() {
        return isRoot;
    }

    public List<File> getSubFiles() {
        return subFiles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSharedUri(String sharedUri) {
        this.sharedUri = sharedUri;
    }

    public void setValiditySharing(Date validitySharing) {
        this.validitySharing = validitySharing;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setParentFolder(File parentFolder) {
        this.parentFolder = parentFolder;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public void setSubFiles(List<File> subFiles) {
        this.subFiles = subFiles;
    }

    @Override
    public String toString() {
        return "File{" + "id=" + id + ", name=" + name + ", fileSize=" + fileSize + ", path=" + path + ", sharedUri=" + sharedUri + ", validitySharing=" + validitySharing + ", fileType=" + fileType + ", ext=" + ext + ", user=" + user + ", parentFolder=" + parentFolder + ", isRoot=" + isRoot + ", subFiles=" + subFiles + '}';
    }
}
