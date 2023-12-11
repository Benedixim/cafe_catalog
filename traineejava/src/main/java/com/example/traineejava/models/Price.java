package com.example.traineejava.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "prices")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//автоинкремент
    private Long id_price;

    @ManyToOne
    private Dish dish;

    private Date date;

    private float price;

    public Price(Dish dish, Date date, float price) {
        this.dish = dish;
        this.date = date;
        this.price = price;
    }

    public Price() {
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
