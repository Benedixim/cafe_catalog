package com.example.traineejava.models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String author;

    private String linkPhoto;
    @ManyToOne
    private Dish title;
    private String ingredients;
    private String servings;
    private String instructions;

    public Recipe() {
    }

    public Recipe(Dish title, String ingredients, String servings, String instructions) {
        this.title = title;
        this.ingredients = ingredients;
        this.servings = servings;
        this.instructions = instructions;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(String linkPhoto) {
        this.linkPhoto = linkPhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dish getTitle() {
        return title;
    }

    public void setTitle(Dish title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
