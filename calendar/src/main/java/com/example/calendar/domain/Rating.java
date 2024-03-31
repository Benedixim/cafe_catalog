package com.example.calendar.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "ratings")
@Data
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//автоинкремент
    private Long id_rating;


    private float rating;

    private float staff;
    private float atmosphere;
    private float service;
    private float price;
    private float cookery;

    public Rating(float staff, float atmosphere, float service, float price, float cookery) {
        this.staff = staff;
        this.atmosphere = atmosphere;
        this.service = service;
        this.price = price;
        this.cookery = cookery;
    }

    public Rating(){

    }

    public Rating(float rating, float staff, float atmosphere, float service, float price, float cookery) {
        this.rating = rating;
        this.staff = staff;
        this.atmosphere = atmosphere;
        this.service = service;
        this.price = price;
        this.cookery = cookery;
    }

    public Rating(Float f) {
        this.rating = f;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getStaff() {
        return staff;
    }

    public void setStaff(float staff) {
        this.staff = staff;
    }

    public float getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(float atmosphere) {
        this.atmosphere = atmosphere;
    }

    public float getService() {
        return service;
    }

    public void setService(float service) {
        this.service = service;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCookery() {
        return cookery;
    }

    public void setCookery(float cookery) {
        this.cookery = cookery;
    }
}

