package com.example.traineejava.controllers;

import com.example.traineejava.models.Cafe;
import com.example.traineejava.models.Category;
import com.example.traineejava.models.Dish;
import com.example.traineejava.repo.CafeRepository;
import com.example.traineejava.repo.CategoryRepository;
import com.example.traineejava.repo.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CafeRepository cafeRepository;


    @GetMapping("/categories")
    public String categoryMain(Model model)
    {
        Iterable<Category> categories = categoryRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("posts", categories);//передаем в шаблон

        return "category/category-main";
    }


    @GetMapping("/categories/add")
    public String categoryAdd(Model model)
    {
        Iterable<Cafe> cafes = cafeRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("cafes", cafes);//передаем в шаблон
        return "category/category-add";
    }

    @PostMapping("/categories/add")
    public String categoryAdd(@RequestParam String name, @RequestParam String description, @RequestParam String link, Model model)
    {
        //Cafe cafeObj = cafeRepository.findByName(cafe); // Получаем объект Category по имени
        Category category = new Category(name, description, link);
        //category.setCafes(cafeObj);
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories/{idCaT}")
    public String categoryDetails(@PathVariable(value = "idCaT") long idCaT, Model model)
    {
        if(!categoryRepository.existsById(idCaT)){
            return "redirect:/categories";
        }
        Optional<Category> category = categoryRepository.findById(idCaT);
        ArrayList<Category> res = new ArrayList<>();
        category.ifPresent(res::add);
        model.addAttribute("category", res);
        return "category/category-details";
    }

    @GetMapping("/categories/{idCaT}/edit")
    public String categoryEdit(@PathVariable(value = "idCaT") long id, Model model)
    {

        if(!categoryRepository.existsById(id)){
            return "redirect:/categories";
        }


        Optional<Category> category = categoryRepository.findById(id);
        ArrayList<Category> res = new ArrayList<>();
        category.ifPresent(res::add);
        model.addAttribute("category", res);



        Iterable<Dish> dishes = dishRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("dishes", dishes);//передаем в шаблон


        return "category/category-edit";
    }

    @PostMapping("/categories/{idCaT}/edit")
    public String categoryUpdate(@PathVariable(value = "idCaT") long id, @RequestParam String name, @RequestParam String linkPhoto, @RequestParam String description, @RequestParam List<String> dishes, @RequestParam List<String> dishesAdd,  Model model)
    {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setName(name);
        category.setDescription(description);
        category.setLinkPhoto(linkPhoto);



        for (String dishName : dishes) {

            Dish dish = dishRepository.findByName(dishName);
            category.deleteDish(dish);
            dish.deleteCategory();
        }

       // List<Dish> selectedDishes = new ArrayList<>();
        for (String dishName : dishesAdd) {

            Dish dish = dishRepository.findByName(dishName);
            category.addDish(dish);
            dish.setCategory(category);
        }

        //category.setCategories(selectedCategories);



        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @PostMapping("/categories/{idCat}/remove")
    public String categoryDelete(@PathVariable(value = "idCat") long id, Model model)
    {

        Category category = categoryRepository.findById(id).orElseThrow();

        Iterable<Dish> dishes = dishRepository.findAll();
        for(Dish dish: dishes){
            if(dish.getCategory().equals(category)) dishRepository.delete(dish);
        }

        categoryRepository.delete(category);



        return "redirect:/categories";
    }
}

