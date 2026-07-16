package com.anton.sportshop.service;

import com.anton.sportshop.model.AppUser;
import com.anton.sportshop.model.Item;
import com.anton.sportshop.model.Rating;
import com.anton.sportshop.repository.ItemRepository;
import com.anton.sportshop.repository.RatingRepository;
import com.anton.sportshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository, ItemRepository itemRepository){
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public int getRating(Long itemId, Long userId){
        Rating rating = ratingRepository.findByUserIdAndItemId(userId,itemId).orElseThrow();
        return rating.getStars();
    }

    public double getAverageRating(Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow();
        return item.getAverage_rating();
    }

    public void rateProduct(Long itemId, Long userId, int stars){
        Item item = itemRepository.findById(itemId).orElseThrow();

        AppUser user = userRepository.findById(userId).orElseThrow();

        Rating rating = ratingRepository.findByUserIdAndItemId(userId, itemId).orElse(new Rating());

        rating.setItem(item);
        rating.setUser(user);
        rating.setStars(stars);

        ratingRepository.save(rating);

        updateAverage(item);
    }

    private void updateAverage(Item item){
        List<Rating> ratings = ratingRepository.findByItemId(item.getId());

        double average = ratings.stream().mapToInt(Rating::getStars).average().orElse(0.0);

        item.setAverage_rating(average);
        item.setTotal_ratings(ratings.size());

        itemRepository.save(item);

    }

    public void deleteUserRating(Long itemId, Long userId){
        Rating rating = ratingRepository.findByUserIdAndItemId(userId,itemId).orElseThrow();
        ratingRepository.delete(rating);
    }
}
