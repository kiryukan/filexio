/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.security;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rvw.itech.filexio.service.UserService;

/**
 *
 * @author renj
 */
@Component
public class JwtFilter extends OncePerRequestFilter{
    
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        
        String authorization = httpServletRequest.getHeader("Authorization");
        String token = null;
        String userName = null;
        
        if(authorization != null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
            try{
                userName = jwtUtility.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
               System.out.println("JWT Token has expired");
            }
        }else {
         System.out.println("Bearer String not found in token");
         //filterChain.doFilter(httpServletRequest, httpServletResponse);
        }    
        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.loadUserByUsername(userName);
            if(jwtUtility.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
