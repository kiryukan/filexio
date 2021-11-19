/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rvw.itech.filexio.storage;

/**
 *
 * @author renj
 */

import org.springframework.beans.factory.annotation.Autowired;

/*@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}*/
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import javax.servlet.MultipartConfigElement;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import rvw.itech.filexio.model.FileType;
import rvw.itech.filexio.model.User;
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
          //System.out.println("user id: " + userId);
          User user = this.userService.getUser(userId);
          System.out.println("retrieved user: " + user.getUsername());
          //String p = "C:/Users/kiryu/Desktop/Projets/Java/filexio-files/" + pathUri + "/";
          String p = this.basePath + pathUri + "/";
          //System.out.println(p);
          this.path = Paths.get(p);
          //System.out.println("retrieved path: " + p);
          String fileName = file.getOriginalFilename();
          //System.out.println("original filename: " + fileName);
          Long fileSize = file.getSize();
          //System.out.println("file size: " + fileSize);
          String fileExt = FilenameUtils.getExtension(fileName);
          //System.out.println("file ext: " + fileExt);
          FileType fileType = this.fileTypeService.getFileTypeByExt(fileExt);
          //System.out.println("retrieved fileType: " + fileType.toString());
          File parentFolder = this.fileService.getFileFromFileId(parentId);
          //System.out.println("retrieved parentFolder: " + parentFolder.getName());

          File f = new File();
          f.setName(fileName);
          f.setFileSize(fileSize);
          f.setPath(pathUri + "/");
          f.setFileType(fileType);
          f.setExt(fileExt);
          f.setUser(user);
          f.setParentFolder(parentFolder);
          f.setIsRoot(false);
          f.setSubFiles(new ArrayList<>());
          f.setValiditySharing(new Date());
          f.setSharedUri("");


          // ### - copy file to destination folder
          //System.out.println("file copy: "+this.path.resolve(file.getOriginalFilename()));
          System.out.println("path in directory: " + this.path.toString());
          Files.copy(file.getInputStream(), this.path.resolve(file.getOriginalFilename()));
          // ### - save file record to database
          //System.out.println("file record: "+f.toString());
          this.fileService.addFile(f);

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
        
    // config for multipart file
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofGigabytes(2));
        factory.setMaxRequestSize(DataSize.ofGigabytes(2));
        return factory.createMultipartConfig();
    }
}
