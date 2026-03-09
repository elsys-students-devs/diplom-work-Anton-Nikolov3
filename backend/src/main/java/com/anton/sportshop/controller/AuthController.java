package com.anton.sportshop.controller;

import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.Cart;
import com.anton.sportshop.repository.UserRepository;
import com.anton.sportshop.service.AppUserDetailsService;
import com.anton.sportshop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")

public class AuthController {


    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder encoder,
                          AppUserDetailsService appUserDetailsService, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.appUserDetailsService = appUserDetailsService;
        this.jwtUtil = jwtUtil;
    }



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUser user){
        user.setRole("USER");
        user.setCart(new Cart(user));
        user.setRole("USER");
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Registered!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppUser user) {

        UserDetails loadedUser = appUserDetailsService
                .loadUserByUsername(user.getUsername());

        if (encoder.matches(user.getPassword(), loadedUser.getPassword())) {

            String token = jwtUtil.generateToken(loadedUser);

            boolean isAdmin = loadedUser.getAuthorities()
                    .stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("isAdmin", isAdmin);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid password or username");
    }

}
