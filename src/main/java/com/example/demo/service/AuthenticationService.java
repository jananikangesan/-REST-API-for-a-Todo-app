package com.example.demo.service;

import com.example.demo.model.AuthenticationResponse;
import com.example.demo.model.User;

public interface AuthenticationService {

 public User register(User request);

 public AuthenticationResponse authenticate(User request);
}
