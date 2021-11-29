/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.utility;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author renj
 */
@Service
public class UtilityIOService {
    
    @Value("${files.base-directory}")
    private String basePath;
    
    public UtilityIOService(){}
    
    public void createFolder(String baseUserUri, String folderName){
        File folder = new File(this.basePath + "/" + baseUserUri + folderName);
        if(folder.mkdir()){
            System.out.println("Folder " + baseUserUri + "/" + folderName +" successfully created");
        }
        else{
            System.out.println("Error cannot create folder "+ baseUserUri + folderName);
        }
    }
    
    public String createRootFolder(String rootFolderName){
        File folder = new File(rootFolderName);
        if(folder.mkdir()){
            System.out.println("Root Folder " + rootFolderName +" successfully created");
        }
        else{
            System.out.println("Error cannot create folder "+ rootFolderName);
        }
        return rootFolderName;
    }
    

    public void deleteFolderRecursively(String pathUri, boolean isCompletePath){
        String fullPath = "";
        if(!isCompletePath){
            fullPath = this.basePath + pathUri;
        }
        else{
            fullPath = pathUri;
        }
        java.io.File ioFile = new java.io.File(fullPath);
        System.out.println("folder selected to delete" + ioFile.getName());
        File[] allContents = ioFile.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteFolderRecursively(file.getAbsolutePath(), true);
            }
        }
        ioFile.delete();
    }

    public void deleteFile(String pathUri){
        String fullPath = this.basePath + pathUri;
        java.io.File ioFile = new java.io.File(fullPath);
        ioFile.delete();
    }
}
