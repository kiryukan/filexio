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
    
    @Query(value="SELECT * FROM File f WHERE f.parent_folder_id = ?1", nativeQuery = true)
    List<File> getFilesFromFolderId(Long id);
    
    List<File> findByParentFolder(Long id);
    
    @Query(value="SELECT * FROM File f WHERE file.path = ?1", nativeQuery = true)
    List<File> getFilesFromPath(String path);
    
    @Query(value="SELECT * FROM File f WHERE f.user_id = ?1", nativeQuery = true)
    List<File> getFilesFromUserId(Long id);
    
    @Query(value="SELECT SUM(f.file_size) FROM File f WHERE f.parent_folder_id = ?1", nativeQuery = true)
    Long getFolderSize(Long id);
}
