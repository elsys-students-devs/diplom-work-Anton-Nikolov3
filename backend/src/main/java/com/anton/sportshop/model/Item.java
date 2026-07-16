package com.anton.sportshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String category;

    private String image_url;

    private Double price;

    @NotNull
    private Double average_rating = 0.0;

    @NotNull
    private Integer total_ratings = 0;

    @ElementCollection
    @CollectionTable(name = "item_types", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "type")
    private List<String> types;
}
