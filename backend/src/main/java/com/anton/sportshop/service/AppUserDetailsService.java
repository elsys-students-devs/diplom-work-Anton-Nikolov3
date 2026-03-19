package com.anton.sportshop.service;

import com.anton.sportshop.dto.user.RegisterUserRequestDTO;
import com.anton.sportshop.dto.user.UserResponseDTO;
import com.anton.sportshop.exception.ResourceConflictException;
import com.anton.sportshop.exception.ResourceNotFoundException;
import com.anton.sportshop.mapper.AppUserMapper;
import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AppUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository, AppUserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO registerUser(RegisterUserRequestDTO request){
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new ResourceConflictException("Username already exists");
        }

        AppUser user = userMapper.registerToEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build();
    }

    public AppUser loadAppUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
