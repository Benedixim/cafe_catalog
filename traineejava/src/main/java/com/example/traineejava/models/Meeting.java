package com.example.traineejava.models;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.List;

@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Time startTime;
    private Time endTime;

    @ManyToOne
    private Cafe cafe;
    private String description;
    @ManyToMany
    private List<User> users;

}
