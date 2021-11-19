import { FileType } from "./filetype.model";
import { User } from "./user.model";

export class File{
  id: number;
  name: string;
  fileSize: number;
  path: string;
  sharedUri: string;
  validitySharing: Date;
  fileType: FileType;
  //fileTypeIcon: string;
  ext: string;
  user: User;
  isRoot: boolean;
  parentFolder: File;
  subFiles: number[];

  constructor(
    id: number, 
    name: string, 
    fileSize: number, 
    path: string, 
    sharedUri: string, 
    validitySharing: Date,
    fileType: FileType,
    //fileTypeIcon: string,
    ext: string,
    user: User,
    isRoot: boolean,
    parentFolder: File,
    subFiles: number[]){
      this.id = id;
      this.name = name;
      this.fileSize = fileSize;
      this.path = path;
      this.sharedUri = sharedUri;
      this.validitySharing = validitySharing;
      this.fileType = fileType;
      //this.fileTypeIcon = fileTypeIcon;
      this.ext = ext;
      this.user = user;
      this.isRoot = isRoot;
      this.parentFolder = parentFolder;
      this.subFiles = subFiles;
    }
}
