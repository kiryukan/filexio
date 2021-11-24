/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rvw.itech.filexio.model.FileType;

/**
 *
 * @author renj
 */
public interface FileTypeRepository extends JpaRepository<FileType, Long> {
    
    @Query(value="SELECT ft FROM FileType as ft WHERE ft.ext = ?1")
    FileType findByExt(String ext);
}
