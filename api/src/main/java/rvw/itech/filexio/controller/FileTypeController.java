/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.controller;

import java.util.List;
import javax.tools.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rvw.itech.filexio.model.File;
import rvw.itech.filexio.model.FileType;
import rvw.itech.filexio.service.FileService;
import rvw.itech.filexio.service.FileTypeService;

/**
 *
 * @author renj
 */
@RestController
@RequestMapping("/api/filetype")
@CrossOrigin(origins="*")
public class FileTypeController {
    @Autowired
    private FileTypeService fileTypeService;

    public FileTypeController() {}
    
    @GetMapping("/get/{id}")
    public ResponseEntity<FileType> getFileById(@PathVariable("id") Long id){
        FileType fileType = this.fileTypeService.getFileTypeFromId(id);
        return new ResponseEntity<>(fileType, HttpStatus.OK);
    }
    
    @GetMapping("/get/all")
    public ResponseEntity<List<FileType>> getAllFileTypes(){
        List<FileType> fileTypes = this.fileTypeService.getAllFileTypes();
        return new ResponseEntity<>(fileTypes, HttpStatus.OK);
    }
    
    @PostMapping("/add")
    public ResponseEntity<FileType> addFile(@RequestBody FileType fileType){
        FileType newFileType = this.fileTypeService.addFileType(fileType);
        return new ResponseEntity<>(newFileType, HttpStatus.OK);
    }
    
    @PostMapping("/update")
    public ResponseEntity<FileType> updateFileType(@RequestBody FileType fileType){
        FileType fl = this.fileTypeService.updateFileType(fileType);
        return new ResponseEntity<>(fl, HttpStatus.OK);
    }
    
    @GetMapping("/delete/{id}")
    public ResponseEntity<List<FileType>> deleteFile(@PathVariable("id") Long id){
        this.fileTypeService.deleteFileType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
