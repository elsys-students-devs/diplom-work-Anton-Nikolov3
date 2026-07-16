package com.anton.sportshop.controller;

import com.anton.sportshop.dto.rating.RatingCreateRequestDTO;
import com.anton.sportshop.dto.rating.UserRatingGetRequestDTO;
import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.Rating;
import com.anton.sportshop.service.AppUserDetailsService;
import com.anton.sportshop.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingsService;
    private final AppUserDetailsService appUserDetailsService;

    public RatingController(RatingService ratingsService, AppUserDetailsService appUserDetailsService){
        this.ratingsService = ratingsService;
        this.appUserDetailsService =appUserDetailsService;
    }

    @PostMapping
    public ResponseEntity<?> rateProduct(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody RatingCreateRequestDTO ratingDTO){

        Long userId = appUserDetailsService.loadAppUserByUsername(user.getUsername()).getId();

        ratingsService.rateProduct(ratingDTO.itemId(), userId, ratingDTO.stars());
        return ResponseEntity.ok("Rating Saved");
    }

    @PostMapping("/user")
    public ResponseEntity<?> getProductRating(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody UserRatingGetRequestDTO ratingDTO){

        Long userId = appUserDetailsService.loadAppUserByUsername(user.getUsername()).getId();

        return ResponseEntity.ok(ratingsService.getRating(ratingDTO.itemId(), userId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getProductAverageRating(@PathVariable Long itemId){
        return ResponseEntity.ok(ratingsService.getAverageRating(itemId));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteUserRating(@AuthenticationPrincipal UserDetails user, @PathVariable Long itemId){
        AppUser appUser = appUserDetailsService.loadAppUserByUsername(user.getUsername());
        ratingsService.deleteUserRating(itemId, appUser .getId());
        return ResponseEntity.noContent().build();
    }
}
