package rvw.itech.filexio.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import rvw.itech.filexio.model.File;
import rvw.itech.filexio.model.FileType;
import rvw.itech.filexio.model.User;
import rvw.itech.filexio.service.FileService;
import rvw.itech.filexio.service.FileTypeService;
import rvw.itech.filexio.storage.ResponseMessage;
import rvw.itech.filexio.storage.StorageService;
import rvw.itech.filexio.storage.FileInfo;
import rvw.itech.filexio.storage.FilesSystemStorageService;
import rvw.itech.filexio.storage.StorageProperties;

@Controller
@RequestMapping("/api/upload")
@CrossOrigin(origins="*")
public class FileUploadController {

  @Autowired
  private StorageService storageService;
  @Autowired
  private FilesSystemStorageService filesSystemStorageService;
  @Autowired
  private FileService fileService;
  @Autowired
  private FileTypeService fileTypeService;
  @Autowired
  private StorageProperties storageProperties;
  
  @PostMapping("/file")
  @CrossOrigin
  @ResponseBody
  public ResponseEntity<ResponseMessage> uploadFileToUserDirectory(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId, @RequestParam("path") String pathUri, @RequestParam("parentId") Long parentId) {
    String message = "";
    try {
      System.out.println("UserId:" + userId + " pathUri:" + pathUri + " parentId:" + parentId);
      this.storageService.saveFileToSelectedDirectory(file, userId, pathUri, parentId);
      File fileDB =  new File();
      String path = this.filesSystemStorageService.getBasePath() + pathUri + "/";
      User user = new User();
      FileType ft = new FileType();
      File parentFolder = new File();
      String ext = FilenameUtils.getExtension(file.getOriginalFilename());
      ft = this.fileTypeService.getFileTypeByExt(ext);
      parentFolder.setId(parentId);
      user.setId(userId);
      fileDB.setUser(user);
      fileDB.setFileType(ft);
      fileDB.setParentFolder(parentFolder);
      fileDB.setExt(ext);
      fileDB.setPath(path);
      this.fileService.addFile(fileDB);
      
      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/files/infos")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
          .fromMethodName(FileUploadController.class, "getFile", path.getFileName().toString()).build().toString();

      return new FileInfo(filename, url);
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    Resource file = storageService.load(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
}
