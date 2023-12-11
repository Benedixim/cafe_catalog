package com.example.traineejava.models.recipecommands;

import com.example.traineejava.models.Recipe;

import java.util.List;

public interface RecipeCommand {
    List<Recipe> execute();
}