package com.example.traineejava.models.recipecommands;

import com.example.traineejava.models.Recipe;
import com.example.traineejava.repo.RecipeRepository;

import java.util.List;

public class SaveRecipeCommand implements RecipeCommand {
    private final RecipeRepository recipeRepository;
    private final Recipe recipe;

    public SaveRecipeCommand(RecipeRepository recipeRepository, Recipe recipe) {
        this.recipeRepository = recipeRepository;
        this.recipe = recipe;
    }

    @Override
    public List<Recipe> execute() {
        recipeRepository.save(recipe);
        return null;
    }
}
