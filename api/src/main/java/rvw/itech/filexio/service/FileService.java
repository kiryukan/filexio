/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.service;
import rvw.itech.filexio.utility.UtilityIOService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rvw.itech.filexio.model.File;
import rvw.itech.filexio.model.FileType;
import rvw.itech.filexio.model.User;
import rvw.itech.filexio.repository.FileRepository;

/**
 *
 * @author renj
 */
@Service
public class FileService {
    private final FileRepository fileRepository;
    private UserService userService;
    private FileTypeService fileTypeService;
    private UtilityIOService utilityIOService;

    @Autowired
    public FileService(FileRepository fileRepository, UserService userService, FileTypeService fileTypeService, UtilityIOService utilityIOService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
        this.fileTypeService = fileTypeService;
        this.utilityIOService = utilityIOService;
    }
    
    public File getFileFromFileId(Long id){
        //return this.fileRepository.findById(id).orElseThrow(()->new ObjectNotFoundException("Cannot find file "+id));
        return this.fileRepository.getById(id);
    }
    
    
    public List<File> getFilesFromFolderId(Long id){
        return this.fileRepository.getFilesFromFolderId(id);
    }
    
    public List<File> findByParentFolder(Long id){
        List<File> files = this.fileRepository.getFilesFromFolderId(id);
        return files;
    }
    
    public List<File> getFilesFromPath(String path){
        return this.fileRepository.getFilesFromPath(path);
    }
    
    public List<File> getFilesFromUserId(Long id){
        return this.fileRepository.getFilesFromUserId(id);
    }
    
    public File addFile(File file){
        //System.out.println(file.getUser().getId());
        User u = this.userService.getUser(file.getUser().getId());
        FileType ft = this.fileTypeService.getFileTypeById(file.getFileType().getId());
        file.setUser(u);
        file.setFileType(ft);
        if(ft.getId() == 1){
	    System.out.println("Create physical folder");
            this.utilityIOService.createFolder(file.getPath(), file.getName());
        }
        else{
	    //System.out.println("Record parent size folder in db");
            File folder = this.fileRepository.getById(file.getParentFolder().getId());
            folder.setFileSize(this.getFolderSize(file.getParentFolder().getId()));
            this.fileRepository.save(folder);
        }
	//System.out.println("File to save to string: "+file.toString());
        return this.fileRepository.save(file);
    }

    public File save(File file){
        return this.fileRepository.save(file);
    }
    /* Not tested */
    public List<File> addMultipleFiles(ArrayList<File> files){
        List<File> fileList = files;
        for(int i=0; i<files.size(); i++){
            fileList.add(files.get(i));
        }
        this.fileRepository.saveAll(fileList);
        return fileList;
    }
    /* Not tested */
    public File updateFile(File file){
        return this.fileRepository.save(file);
    }
    /* Not tested */
    public void deleteFile(Long id){
        File file = this.fileRepository.getById(id);
        System.out.println("path of file to delete: " + file.getPath() + file.getName());
        //String path = "C:/Users/kiryu/Desktop/Projets/Java/filexio-files/" + file.getPath() + file.getName();
        //java.io.File ioFile = new java.io.File(path);
	    String pathUri =  file.getPath() + file.getName();
        System.out.println(file.getFileType().getId());
        if(file.getFileType().getId() == 1){
            this.utilityIOService.deleteFolderRecursively(pathUri, false);
        }
        else{
            this.utilityIOService.deleteFile(pathUri);
        }
        System.out.println(pathUri);
        this.fileRepository.deleteById(id);
    }
    
    public long getFolderSize(Long folderId){
        return this.fileRepository.getFolderSize(folderId);
    }
    
    public void setRecursiveFolderSize(Long folderId){
        File folder = this.fileRepository.getById(folderId);
        List<File> files = this.fileRepository.getFilesFromFolderId(folderId);
        Long folderSize = 0L;
        for(File file:files){
            if(file.getFileType().getId() == 1){
                setRecursiveFolderSize(file.getId());
            }
        }
        folderSize = this.fileRepository.getFolderSize(folder.getId());
        if(folderSize == null)folderSize=0L;
        folder.setFileSize(folderSize);
        this.fileRepository.save(folder);
    }
    
    /* FOLDERS MANAGING */
    public void createFolder(String actualPath, String newFolderName){
        this.utilityIOService.createFolder(actualPath, newFolderName);
    }
    
    public File createNewRootFolderToSaveToDB(String Name, User user){
        File file = new File();
        file.setName(Name);
        file.setUser(user);
        FileType ft = this.fileTypeService.getFileTypeById(1L);
        file.setFileType(ft);
        file.setExt("none");
        file.setFileSize(0L);
        file.setIsRoot(true);
        file.setValiditySharing(new Date());
        File createdFile = this.fileRepository.save(file);
        return createdFile;
    }
}
