package com.example.traineejava.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//автоинкремент
    private Long idDish;

    private String name; //название

    private java.lang.String description;

    private float price;

    private int weight;

    private String linkPhoto;

    @ManyToOne
    private Category category;

    private float fat, carbon, protein;

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getCarbon() {
        return carbon;
    }

    public void setCarbon(float carbon) {
        this.carbon = carbon;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public Long getIdDish() {
        return idDish;
    }

    public void setIdDish(Long idDish) {
        this.idDish = idDish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getWeight() {

        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(String linkPhoto) {
        this.linkPhoto = linkPhoto;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Dish() {
    }

    public Dish(String name, String description, float price, int weight, String linkPhoto, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.linkPhoto = linkPhoto;
        this.category = category;
    }

    public void deleteCategory() {
        this.category=null;
    }



    @Override
    public String toString() {
        return "Dish{" +
                " name='" + name + '\'' +
                '}';
    }
}
