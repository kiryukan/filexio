import { Role } from './role.model';
export interface User{
  id: number;
  username: string;
  email: string;
  password: string;
  role: Role;
  rootFolderName: string,
  lastConnection: string;
  usedDiskSpace: number;
}
