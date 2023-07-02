package com.example.traineejava.repo;

import com.example.traineejava.models.Dish;
import org.springframework.data.repository.CrudRepository;

public interface DishRepository extends CrudRepository<Dish, Long> {

    Dish findByName(String dishName);
}
