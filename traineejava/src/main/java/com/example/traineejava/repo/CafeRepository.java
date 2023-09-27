package com.example.traineejava.repo;

import com.example.traineejava.models.Cafe;
import com.example.traineejava.models.Dish;
import org.springframework.data.repository.CrudRepository;
import java.sql.Time;

import java.util.List;

public interface CafeRepository extends CrudRepository<Cafe, Long> {

    List<Cafe> findByName(String cafe);

    List<Cafe> findByRatingBetween(float f, float s);

    List<Cafe> findByDishesContains(Dish dish);

    List<Cafe> findByOpenBeforeAndCloseAfterOrderByMoneyDesc(Time start, Time end);

    Iterable<Cafe> findByNameContainingIgnoreCase(String name);

    Iterable<Cafe> findByNameContainingIgnoreCaseAndRatingBetween(String name, float v, float v1);

   // Iterable<Cafe> findByDishesContains(List<Dish> dishList);


}
