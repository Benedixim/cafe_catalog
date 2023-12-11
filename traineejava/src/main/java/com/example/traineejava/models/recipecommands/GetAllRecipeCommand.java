package com.example.traineejava.models.recipecommands;

import com.example.traineejava.models.Recipe;
import com.example.traineejava.repo.RecipeRepository;

import java.util.List;

public class GetAllRecipeCommand implements RecipeCommand {
    private final RecipeRepository recipeRepository;

    public GetAllRecipeCommand(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> execute() {
        return recipeRepository.findAll();
    }
}