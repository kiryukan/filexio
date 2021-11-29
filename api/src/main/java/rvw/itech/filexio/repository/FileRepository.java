/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rvw.itech.filexio.model.File;

/**
 *
 * @author renj
 */
public interface FileRepository extends JpaRepository<File, Long>{
    
    @Query(value="SELECT f FROM File f WHERE f.parentFolder.id = ?1")
    List<File> getFilesFromFolderId(Long id);
    
    List<File> findByParentFolder(Long id);
    
    @Query(value="SELECT f FROM File f WHERE f.path = ?1")
    List<File> getFilesFromPath(String path);
    
    @Query(value="SELECT f FROM File f WHERE f.user.id = ?1")
    List<File> getFilesFromUserId(Long id);
    
    @Query(value="SELECT SUM(f.fileSize) FROM File f WHERE f.parentFolder.id = ?1")
    Long getFolderSize(Long id);
}
