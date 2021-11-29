/**
 *
 * @author renj
 */
package rvw.itech.filexio.storage;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javax.servlet.MultipartConfigElement;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import rvw.itech.filexio.model.File;
import rvw.itech.filexio.service.FileService;
import rvw.itech.filexio.service.FileTypeService;
import rvw.itech.filexio.service.UserService;

@Service
public class FilesSystemStorageService implements StorageService {

    //private final Path root = Paths.get("uploads");
    private final StorageProperties storageProperties = new StorageProperties();
    private Path path;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileTypeService fileTypeService;
    @Value("${files.base-directory}")
    private String basePath;

    @Override
    public void init() { // Init directory if not yet created... only once...
        try {
          Files.createDirectory(path);
        } catch (IOException e) {
          throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
          System.out.println(this.path.getRoot());
          Files.copy(file.getInputStream(), this.path.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
          throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
  
    public void saveFileToSelectedDirectory(MultipartFile file, Long userId, String pathUri, Long parentId) {
        try {
          String p = this.basePath + pathUri;
          System.out.println(p);
          this.path = Paths.get(p);

          // ### - copy file to destination folder
          //System.out.println("file copy: "+this.path.resolve(file.getOriginalFilename()));
          System.out.println("path in directory: " + this.path.toString());
          Files.copy(file.getInputStream(), this.path.resolve(file.getOriginalFilename()));

        } catch (Exception e) {
          throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
          Path file = path.resolve(filename);
          Resource resource = new UrlResource(file.toUri());

          if (resource.exists() || resource.isReadable()) {
            return resource;
          } else {
            throw new RuntimeException("Could not read the file!");
          }
        } catch (MalformedURLException e) {
          throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.path, 1).filter(path -> !path.equals(this.path)).map(this.path::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
  
    @Override
    public void copyFile(File originalFile, File destinationFolder)throws IOException{
        String origPath = this.basePath + originalFile.getPath() + originalFile.getName();
        String destPath = this.basePath + destinationFolder.getPath() + destinationFolder.getName() + "/" + originalFile.getName();
        System.out.println("dest name folder: "+destinationFolder.getName());
        System.out.println("origPath: "+origPath);
        System.out.println("destPath: "+destPath);
        java.io.File original = new java.io.File(origPath);
        java.io.File copied = new java.io.File(destPath);
        if(originalFile.getFileType().getId() != 1)
        {
            // IF FILE
            FileUtils.copyFile(original, copied);          
        }
        else{
            // IF DIRECTORY
            FileUtils.copyDirectory(original, copied);     
        }
    }
  
    @Override
    public void moveFile(File originalFile, File destinationFolder)throws IOException{
        String origPath = this.basePath + originalFile.getPath() + originalFile.getName();
        String destPath = this.basePath + destinationFolder.getPath() + destinationFolder.getName() + "/" + originalFile.getName();
        System.out.println("dest name folder: "+destinationFolder.getName());
        System.out.println("origPath: "+origPath);
        System.out.println("destPath: "+destPath);
        java.io.File original = new java.io.File(origPath);
        java.io.File copied = new java.io.File(destPath);
        if(originalFile.getFileType().getId() != 1)
        {
            // IF FILE
            FileUtils.copyFile(original, copied);          
        }
        else{
            // IF DIRECTORY
            FileUtils.copyDirectory(original, copied);     
        }
        original.delete();
    }
  
    public void renameFile(File originalFile, String newName)throws IOException{
        String origPath = this.basePath + originalFile.getPath() + originalFile.getName();
        String newFileName = this.basePath + originalFile.getPath() + newName;
        java.io.File original = new java.io.File(origPath);
        java.io.File newFile = new java.io.File(newFileName);
        original.renameTo(newFile);
    }

    public String getFileUrl(String uri, String fileName){
        return this.basePath + uri + fileName;
    }
 
    public Resource download(String filename, String filesPath) {
        System.out.println("path: "+filesPath);
        try {
            Path file = Paths.get(this.basePath + filesPath)
                             .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String getBasePath(){
	return this.basePath;
    }
        
    // config for multipart file
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofGigabytes(2));
        factory.setMaxRequestSize(DataSize.ofGigabytes(2));
        return factory.createMultipartConfig();
    }
}
