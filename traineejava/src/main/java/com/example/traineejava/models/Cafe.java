package com.example.traineejava.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cafes")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//автоинкремент
    private Long idCafe;

    @Column(columnDefinition="varchar(1000)")
    private java.lang.String description;

    private int views;
    private float rating;

    private String linkPhoto, name, address;

    @ManyToMany
    private List<Dish> dishes;


    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }


    public Long getIdCafe() {
        return idCafe;
    }

    public void setIdCafe(Long idCafe) {
        this.idCafe = idCafe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(String linkPhoto) {
        this.linkPhoto = linkPhoto;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Cafe() {
    }

    public Cafe(String description, String linkPhoto, String name, String address) {
        this.description = description;
        this.linkPhoto = linkPhoto;
        this.name = name;
        this.address = address;
        this.views = 0;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void oneMore()
    {
        this.views+=1;
    }

    public void addDish(Dish dish) {
//        if(categories.contains(dish.getCategory()))
//        {dishes.add(dish);
//         makeUniqCat();}
//        else dishes.add(dish);

        if(!dishes.contains(dish)) dishes.add(dish);


    }



    public void makeUniqDish(){
        for (Dish dish: dishes) {
            if(dishes.indexOf(dish)!=dishes.lastIndexOf(dish)) dishes.remove(dish);
        }
    }

    public void delDish(Dish dish) { if(dishes.contains(dish)) dishes.remove(dish);    }


    @Override
    public String toString() {
        return "Cafe{" +
                " name='" + name + '\'' +
                '}';
    }
}
