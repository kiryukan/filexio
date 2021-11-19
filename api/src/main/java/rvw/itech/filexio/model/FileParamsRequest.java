/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rvw.itech.filexio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author renj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileParamsRequest {
    private Long userId;
    private String pathUri;
    private Long parentId;
}
