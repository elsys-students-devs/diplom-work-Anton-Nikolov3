package com.anton.sportshop.mapper;

import com.anton.sportshop.dto.user.LoginUserRequestDTO;
import com.anton.sportshop.dto.user.RegisterUserRequestDTO;
import com.anton.sportshop.dto.user.UserResponseDTO;
import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.Cart;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {
    public AppUser registerToEntity (RegisterUserRequestDTO requestDTO){
        AppUser user = new AppUser();

        user.setUsername(requestDTO.username());
        user.setEmail(requestDTO.email());

        user.setRole("USER");

        Cart cart = new Cart(user);
        user.setCart(cart);

        return user;
    }

    public AppUser loginToEntity(LoginUserRequestDTO requestDTO){
        AppUser user = new AppUser();

        user.setUsername(requestDTO.username());
        return user;
    }

    public UserResponseDTO toDto(AppUser user){
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

}
