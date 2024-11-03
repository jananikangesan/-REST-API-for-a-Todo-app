package com.example.demo.filter;

import com.example.demo.service.JwtService;

import com.example.demo.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  public JwtService jwtService;

  @Autowired
  public UserDetailsServiceImpl userDetailsService;


  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    String authHeader=request.getHeader("Authorization");

    if(authHeader==null || !authHeader.startsWith("Bearer")){
      filterChain.doFilter(request,response);
      return;
    }

    String token=authHeader.substring(7);

    String email=jwtService.extractEmail(token);

    if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){

      UserDetails userDetails=userDetailsService.loadUserByUsername(email);

      if(jwtService.isValid(token,userDetails)){
        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
            userDetails,null,userDetails.getAuthorities()
        );

        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }

    }

    filterChain.doFilter(request,response);
  }
}
