package com.anton.sportshop.controller;

import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.Cart;
import com.anton.sportshop.repository.UserRepository;
import com.anton.sportshop.service.impl.AppUserDetailsService;
import com.anton.sportshop.util.JwtFilter;
import com.anton.sportshop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AppUserDetailsService appUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUser user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setCart(new Cart());
        user.setRole("USER");
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Registered!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppUser user){
        UserDetails loadedUser = appUserDetailsService.loadUserByUsername(user.getUsername());

        if(encoder.matches(user.getPassword(), loadedUser.getPassword())){
            return ResponseEntity.ok(jwtUtil.generateToken(loadedUser));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password or username");

    }

}
