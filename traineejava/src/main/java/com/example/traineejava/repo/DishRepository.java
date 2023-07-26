package com.example.traineejava.repo;

import com.example.traineejava.models.Category;
import com.example.traineejava.models.Dish;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishRepository extends CrudRepository<Dish, Long> {

    Dish findByName(String dishName);

    List<Dish> findByCategory(Category c);

    List<Dish> findByCategoryIsNot(Category c);




}

