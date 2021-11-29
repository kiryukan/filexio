/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Type;

/**
 *
 * @author renj
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FileType implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="type")
    private String type;
    @Column(name="ext")
    private String ext;
    //@Column(name="icon_thin")
    @Type(type="text")
    private String iconThin;
    //@Column(name="icon_filled")
    @Type(type="text")
    private String iconFilled;
    //@Column(name="icon_svg")
    @Type(type="text")
    private String iconSvg;
    
    @OneToMany(fetch = FetchType.LAZY, targetEntity = File.class, mappedBy = "fileType") @JsonIgnore
    private List<File> files = new ArrayList<>();

    public String getIconSvg() {
        return iconSvg;
    }
    
    public String getIconThin() {
        return iconThin;
    }

    public String getIconFilled() {
        return iconFilled;
    }

    public void setIconSvg(String iconSvg) {
        this.iconSvg = iconSvg;
    }

    public void setIconThin(String iconThin) {
        this.iconThin = iconThin;
    }

    public void setIconFilled(String iconFilled) {
        this.iconFilled = iconFilled;
    }

    public FileType(){
        
    }
    
    public FileType(String type, String ext) {
        this.type = type;
        this.ext = ext;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getExt() {
        return ext;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "FileType{" + "id=" + id + ", type=" + type + ", ext=" + ext + ", iconThin=" + iconThin + ", iconFilled=" + iconFilled + ", iconSvg=" + iconSvg + ", files=" + files + '}';
    }
}
