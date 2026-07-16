package com.anton.sportshop.controller;

import com.anton.sportshop.dto.user.LoginUserRequestDTO;
import com.anton.sportshop.dto.user.RegisterUserRequestDTO;
import com.anton.sportshop.mapper.AppUserMapper;
import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.service.AppUserDetailsService;
import com.anton.sportshop.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final PasswordEncoder encoder;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtil jwtUtil;
    private final AppUserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(PasswordEncoder encoder,
                          AppUserDetailsService appUserDetailsService,
                          JwtUtil jwtUtil,
                          AppUserMapper mapper,
                          PasswordEncoder passwordEncoder){
        this.encoder = encoder;
        this.appUserDetailsService = appUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }



    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequestDTO request){
        return ResponseEntity.ok(appUserDetailsService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserRequestDTO loginUserRequestDTO) {
        AppUser user = mapper.loginToEntity(loginUserRequestDTO);
        user.setPassword(passwordEncoder.encode(loginUserRequestDTO.password()));

        UserDetails loadedUser = appUserDetailsService
                .loadUserByUsername(user.getUsername());

        if (encoder.matches(loginUserRequestDTO.password(), loadedUser.getPassword()))  {

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
