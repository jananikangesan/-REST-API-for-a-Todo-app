package com.example.demo.controller;

import com.example.demo.model.AuthenticationResponse;
import com.example.demo.model.User;
import com.example.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

  @Autowired
  public  AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody User request){
    return ResponseEntity.ok(authenticationService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody User request){
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }
}
