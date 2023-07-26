package com.example.traineejava.models;


import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//автоинкремент
    private Long idCaT;

    private String name; //название


    private java.lang.String description;

    private String linkPhoto;

    // остальные поля, конструкторы и методы


    public Long getIdCaT() {
        return idCaT;
    }

    public void setIdCaT(Long idCaT) {
        this.idCaT = idCaT;
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

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(String linkPhoto) {
        this.linkPhoto = linkPhoto;
    }

    public Category() {
    }

    public Category(String name, String description, String linkPhoto) {
        this.name = name;
        this.description = description;
        this.linkPhoto = linkPhoto;
    }

    @Override
    public String toString() {
        return "Category{" +
                " name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}



