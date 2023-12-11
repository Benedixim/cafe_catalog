package com.example.traineejava.repo;

import com.example.traineejava.models.Dish;
import com.example.traineejava.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {


    List<Recipe> findByTitle(Dish dish);
}