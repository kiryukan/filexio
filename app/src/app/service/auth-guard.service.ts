import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "node_modules/@angular/router";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";

@Injectable() // allow to inject another service 
export class AuthGuard implements CanActivate{
    constructor(private authService: AuthService, private router: Router){}
    canActivate(
        route: ActivatedRouteSnapshot, 
        state: RouterStateSnapshot)
        :Observable<boolean> | Promise<boolean> | boolean {
            if(this.authService.isAuthentified){
                return true;
            }else{
                this.router.navigate(['home']);
                return false;
            }
    }
    
    
}