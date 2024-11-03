package com.example.demo.service;

import com.example.demo.model.User;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

  public String extractEmail(String token);
  public <T> T extractClaim(String token, Function<Claims,T> resolver);
  public boolean isValid(String token, UserDetails user);
  public String generateToken(User user);



}
