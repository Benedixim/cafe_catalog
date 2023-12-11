package com.example.traineejava.models.recipecommands;

import com.example.traineejava.models.Dish;
import com.example.traineejava.models.Recipe;
import com.example.traineejava.repo.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class GetByDishRecipeCommand implements RecipeCommand {
    private final RecipeRepository recipeRepository;

    private final Dish dish;

    public GetByDishRecipeCommand(RecipeRepository recipeRepository, Dish dish) {
        this.recipeRepository = recipeRepository;
        this.dish = dish;
    }

    @Override
    public List<Recipe> execute() {
        List<Recipe> filteredRecipes = new ArrayList<>();
        Iterable<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes) {
            if (recipe.getTitle() != null && recipe.getTitle().equals(dish)) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }
}