package com.example.traineejava.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//автоинкремент
    private Long id_com;

    @OneToOne
    private Rating rating;

    private Date date;

    @Column(columnDefinition="varchar(1000)")
    private java.lang.String description;

    @ManyToOne
    private User user;

    @ManyToOne
    private Cafe cafe;

    public Comment(Rating rating, Date date, String description, User user, Cafe cafe) {
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

    public Cafe getCafe() {
        return cafe;
    }

    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
    }
}
