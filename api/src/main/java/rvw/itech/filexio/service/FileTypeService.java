/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rvw.itech.filexio.exception.ObjectNotFoundException;
import rvw.itech.filexio.model.FileType;
import rvw.itech.filexio.repository.FileTypeRepository;

/**
 *
 * @author renj
 */
@Service
public class FileTypeService {
    private final FileTypeRepository fileTypeRepository;

    @Autowired
    public FileTypeService(FileTypeRepository fileTypeRepository) {
        this.fileTypeRepository = fileTypeRepository;
    }
    
    public FileType getFileTypeById(Long id){
        //return this.fileTypeRepository.getById(id);
        return this.fileTypeRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Filetype with "+id+" was not found"));
    }
    
    public FileType getFileTypeByExt(String ext){
        return this.fileTypeRepository.findByExt(ext);
    }
    
    public FileType getFileTypeFromId(Long id){
        return this.fileTypeRepository.getById(id);
    }
    
    public List<FileType> getAllFileTypes(){
        return this.fileTypeRepository.findAll();
    }
    
    public FileType addFileType(FileType fileType){
        return this.fileTypeRepository.save(fileType);
    }
    
    public FileType updateFileType(FileType fileType){
        return this.fileTypeRepository.save(fileType);
    }
    
    public void deleteFileType(Long id){
        this.fileTypeRepository.deleteById(id);
    }
    
}
