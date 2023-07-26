package com.example.traineejava.repo;

import com.example.traineejava.models.Cafe;
import com.example.traineejava.models.Dish;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CafeRepository extends CrudRepository<Cafe, Long> {

    List<Cafe> findByName(String cafe);

    List<Cafe> findByRatingBetween(float f, float s);

    List<Cafe> findByDishesContains(Dish dish);


}
