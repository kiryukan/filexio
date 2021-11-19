/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rvw.itech.filexio.storage;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;
/**
 *
 * @author renj
 */
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import rvw.itech.filexio.model.File;

public interface StorageService {
  public void init();

  public void saveFileToSelectedDirectory(MultipartFile file, Long userId, String pathUri, Long parentId);
  
  public void save(MultipartFile file);

  public Resource load(String filename);

  public void deleteAll();

  public Stream<Path> loadAll();
  
  public void copyFile(File originalFile, File destinationFolder) throws IOException;
  
  public void moveFile(File originalFile, File destinationFolder) throws IOException;
}
