/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.FileObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rvw.itech.filexio.model.File;
import rvw.itech.filexio.model.FileType;
import rvw.itech.filexio.service.FileService;
import rvw.itech.filexio.service.FileTypeService;
import rvw.itech.filexio.storage.FilesSystemStorageService;

/**
 *
 * @author renj
 */
@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins="*")
public class FileController {
    private final FileService fileService;
    @Autowired
    private FilesSystemStorageService filesSystemStorageService;
    @Autowired
    private FileTypeService fileTypeService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<File> getFileById(@PathVariable("id") Long id){
        File file = this.fileService.getFileFromFileId(id);
        return new ResponseEntity<>(file, HttpStatus.OK);
    }
    
    @PostMapping("/add")
    public ResponseEntity<File> addFile(@RequestBody File file){
        //System.out.println(file.toString());
        File newFile = this.fileService.addFile(file);
        return new ResponseEntity<>(newFile, HttpStatus.OK);
    }
    
    @PostMapping("/update")
    public ResponseEntity<File> updateFile(@RequestBody File file){
        File newFile = this.fileService.updateFile(file);
        return new ResponseEntity<>(newFile, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    @CrossOrigin
    public ResponseEntity<List<File>> deleteFile(@PathVariable("id") Long id){
        this.fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping("/copy/{folderId}")
    public ResponseEntity<List<File>> copyFile(@RequestBody Long filesArray[], @PathVariable("folderId") Long folderId) throws IOException{
        List<File> copiedFiles = new ArrayList<>();
        System.out.println("folderId: "+folderId);
        File folder = this.fileService.getFileFromFileId(folderId);
        for(Long fileId : filesArray){
            File file = this.fileService.getFileFromFileId(fileId);
            File fileCopied = new File(file);
            this.filesSystemStorageService.copyFile(file, folder);
            String path = folder.getPath()+folder.getName()+"/";
            
            //fileCopied.setName(file.getName());
            //fileCopied.setExt(file.getExt());
            //fileCopied.setFileType(file.getFileType());
            //fileCopied.setIsRoot(file.getIsRoot());
            //fileCopied.setFileSize(file.getFileSize());
            //fileCopied.setUser(file.getUser());
            fileCopied.setParentFolder(folder);
            fileCopied.setPath(path);
            
            File newFile = this.fileService.addFile(fileCopied);
            copiedFiles.add(newFile);
        }
        
        return new ResponseEntity<>(copiedFiles, HttpStatus.OK);
    }
    
    @PostMapping("/move/{folderId}")
    public ResponseEntity<List<File>> moveFile(@RequestBody Long filesArray[], @PathVariable("folderId") Long folderId) throws IOException{
        List<File> movedFiles = new ArrayList<>();
        for(Long fileId : filesArray){
            File file = this.fileService.getFileFromFileId(fileId);
            File folder = this.fileService.getFileFromFileId(folderId);
            String path = folder.getPath()+folder.getName()+"/";

            this.filesSystemStorageService.moveFile(file, folder);
            file.setPath(path);
            file.setParentFolder(folder);
            File newFile = this.fileService.updateFile(file);
            movedFiles.add(newFile);
        }
        
        return new ResponseEntity<>(movedFiles, HttpStatus.OK);
    }
    
    @PostMapping("/rename")
    @CrossOrigin
    public ResponseEntity<File> renameFile(@RequestBody File renamedFile) throws IOException{
        String ext = "";
        if(renamedFile.getName().lastIndexOf('.') >= 0){
            ext = renamedFile.getName().substring(
                renamedFile.getName().lastIndexOf('.') + 1, 
                renamedFile.getName().length()
            );
            System.out.println("ext file: "+ext);
        }
        FileType fileType = this.fileTypeService.getFileTypeByExt(ext);
        if(fileType != null && !Objects.equals(fileType.getId(), renamedFile.getFileType().getId())){
            renamedFile.setFileType(fileType);
        }
        
        File file = this.fileService.getFileFromFileId(renamedFile.getId());
        this.filesSystemStorageService.renameFile(file, renamedFile.getName());
        File newRenamedFile = this.fileService.updateFile(renamedFile);
        
        return new ResponseEntity<>(newRenamedFile, HttpStatus.OK);
    }
    
    /*@GetMapping("/download/{id}")
    public ResponseEntity<File> downloadFileById(@PathVariable("id") Long id) throws IOException{
        File file = this.fileService.getFileFromFileId(id);
        System.out.println("download path: "+this.filesSystemStorageService.getFileUrl(file.getPath(), file.getName()));
        try {
            FileUtils.copyURLToFile(
                    new URL("file:///" + this.filesSystemStorageService.getFileUrl(file.getPath(), file.getName())),
                    new java.io.File(file.getName()),
                    10000,
                    10000
            );
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(file, HttpStatus.OK);
    }*/
    
    @GetMapping("download/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws IOException {
        File f = this.fileService.getFileFromFileId(fileId);
        
        Resource file = this.filesSystemStorageService.download(f.getName(), f.getPath());
        Path path = file.getFile()
                        .toPath();

        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                             .body(file);
    }
}
