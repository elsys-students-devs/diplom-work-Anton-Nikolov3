package com.anton.sportshop.model;

import jakarta.persistence.*;
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

    @ElementCollection
    @CollectionTable(name = "item_types", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "type")
    private List<String> types;
}
