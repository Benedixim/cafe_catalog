package com.example.traineejava.repo;

import com.example.traineejava.models.Cafe;
import com.example.traineejava.models.Category;
import com.example.traineejava.models.Rating;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating, Long> {


}
