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
    
    public static void createFolder(String baseUserUri, String folderName){
        UtilityIOService utilityIOService = new UtilityIOService();
        String basePath = utilityIOService.getBasePath();
        File folder = new File(basePath + "/" + baseUserUri + folderName);
        if(folder.mkdir()){
            System.out.println("Folder " + baseUserUri + "/" + folderName +" successfully created");
        }
        else{
            System.out.println("Error cannot create folder "+ baseUserUri + folderName);
        }
    }
    
    public static void deleteFolderRecursively(File ioFile){
        System.out.println("folder selected to delete" + ioFile.getName());
        File[] allContents = ioFile.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteFolderRecursively(file);
            }
        }
        ioFile.delete();
    }
    
    public static void deleteFile(File ioFile){
        ioFile.delete();
    }
    
    public String getBasePath(){
        return this.basePath;
    }
    /*public static void deleteFile(String baseUserUri, String folderName){
        File file = new File(baseUserUri + "/" + folderName);
        file.delete();
    }*/
}
