package com.example.traineejava.controllers;

import com.example.traineejava.models.Dish;
import com.example.traineejava.models.Recipe;
import com.example.traineejava.models.recipecommands.GetAllRecipeCommand;
import com.example.traineejava.models.recipecommands.SaveRecipeCommand;
import com.example.traineejava.repo.DishRepository;
import com.example.traineejava.repo.RecipeRepository;
import com.example.traineejava.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RecipeController {

    private final RecipeService recipeService;

    private final DishRepository dishRepository;

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeController(RecipeService recipeService, DishRepository dishRepository, RecipeRepository recipeRepository) {
        this.recipeService = recipeService;
        this.dishRepository = dishRepository;
        this.recipeRepository = recipeRepository;
    }

    @PostMapping("/dishes/{idDish}/edit/add-recipe")
    public String addRecipe(@PathVariable(value = "idDish") long idDish,@RequestParam("ingredients") String ingredients, @RequestParam("servings") String servings, @RequestParam("instructions") String instructions, @RequestParam("name") String name, @RequestParam("linkPhoto") String linkPhoto, @RequestParam("author") String author) {
        Dish dish = dishRepository.findById(idDish).get();
        Recipe recipe = new Recipe(dish, ingredients, servings, instructions);
        recipe.setLinkPhoto(linkPhoto);
        recipe.setName(name);
        recipe.setAuthor(author);

        SaveRecipeCommand saveRecipeCommand = new SaveRecipeCommand(recipeRepository, recipe);
        recipeService.saveRecipe(saveRecipeCommand);

        return "redirect:/dishes/{idDish}";
    }

    @GetMapping("/recipes")
    public String getAllRecipes(Model model) {
        GetAllRecipeCommand getAllRecipeCommand = new GetAllRecipeCommand(recipeRepository);
        List<Recipe> recipes = recipeService.getAllRecipes(getAllRecipeCommand);
        model.addAttribute("recipes", recipes);
        return "recipe/recipe-main";
    }

    @GetMapping("/dishes/{idDish}/recipes/{recipeId}")
    public String editRecipeForm(@PathVariable("recipeId") Long recipeId, Model model) {

        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        model.addAttribute("recipe", recipe);

        return "recipe/recipe-details";
    }

    @GetMapping("/dishes/{idDish}/recipes/{recipeId}/edit")
    public String editRecipeForm(@PathVariable("idDish") Long dishId, @PathVariable("recipeId") Long recipeId, Model model) {

        Recipe recipe = recipeRepository.findById(recipeId).get();
        model.addAttribute("recipe", recipe);

        return "recipe/recipe-edit";
    }

    // Update recipe - POST request
    @PostMapping("/dishes/{idDish}/recipes/{recipeId}/edit")
    public String updateRecipe(@PathVariable("idDish") Long dishId, @PathVariable("recipeId") Long recipeId, @RequestParam("ingredients") String ingredients, @RequestParam("servings") String servings, @RequestParam("instructions") String instructions, @RequestParam("name") String name, @RequestParam("linkPhoto") String linkPhoto, @RequestParam("author") String author) {
        // Retrieve the existing recipe from your data source using dishId and recipeId
        Recipe existingRecipe = recipeRepository.findById(recipeId).get();
        existingRecipe.setName(name);
        existingRecipe.setLinkPhoto(linkPhoto);
        existingRecipe.setIngredients(ingredients);
        existingRecipe.setServings(servings);
        existingRecipe.setInstructions(instructions);
        existingRecipe.setAuthor(author);
        recipeRepository.save(existingRecipe);

        // Call your recipe service or repository to save the updated recipe
        // recipeService.save(existingRecipe);

        return "redirect:/dishes/{idDish}"; // Redirect to the dish details page
    }

    @PostMapping("/dishes/{idDish}/recipes/{recipeId}/delete")
    public String deleteRecipe(@PathVariable("idDish") Long dishId, @PathVariable("recipeId") Long recipeId) {

        Recipe existingRecipe = recipeRepository.findById(recipeId).orElseThrow();
        recipeRepository.delete(existingRecipe);

        return "redirect:/dishes/{idDish}"; // Redirect to the dish details page
    }
}
