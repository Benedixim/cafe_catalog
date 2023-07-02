package com.example.traineejava.repo;

import com.example.traineejava.models.Cafe;
import org.springframework.data.repository.CrudRepository;

public interface CafeRepository extends CrudRepository<Cafe, Long> {

    Cafe findByName(String cafe);
}
