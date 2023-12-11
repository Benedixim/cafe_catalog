package com.example.traineejava.services;

import com.example.traineejava.models.Recipe;
import com.example.traineejava.models.recipecommands.RecipeCommand;
import com.example.traineejava.repo.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {


    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void saveRecipe(RecipeCommand recipeCommand) {
        recipeCommand.execute();
    }

    public List<Recipe> getAllRecipes(RecipeCommand recipeCommand) {
        return recipeCommand.execute();
    }

    public List<Recipe> getByDishRecipes(RecipeCommand recipeCommand) {
        return recipeCommand.execute();
    }

    //Singleton
    private static RecipeService instance;

    public static RecipeService getInstance(RecipeRepository recipeRepository) {
        if (instance == null) {
            synchronized (RecipeService.class) {
                if (instance == null) {
                    instance = new RecipeService(recipeRepository);
                }
            }
        }
        return instance;
    }

}
