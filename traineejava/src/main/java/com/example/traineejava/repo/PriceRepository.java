package com.example.traineejava.repo;



import com.example.traineejava.models.Dish;
import com.example.traineejava.models.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, Long> {
    Iterable<Price> findByDishOrderByDate(Dish dish);
}
