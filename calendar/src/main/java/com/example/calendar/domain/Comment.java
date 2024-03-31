package com.example.calendar.domain;

import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "comments")
//@Data
//@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//автоинкремент
    private Long id_com;

    @OneToOne
    private Rating rating;

    private Date date;

    @Column(columnDefinition="varchar(1000)")
    private String description;

    @ManyToOne
    private User user;

    @ManyToOne
    private Room cafe;

    public Comment(Rating rating, Date date, String description, User user, Room cafe) {
        this.rating = rating;
        this.date = date;
        this.description = description;
        this.user = user;
        this.cafe = cafe;
    }

    public Comment(){

    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getCafe() {
        return cafe;
    }

    public void setCafe(Room cafe) {
        this.cafe = cafe;
    }
}
