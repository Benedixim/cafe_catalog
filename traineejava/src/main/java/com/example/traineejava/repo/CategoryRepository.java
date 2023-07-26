package com.example.traineejava.repo;


import com.example.traineejava.models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Category findByName(String category);


}