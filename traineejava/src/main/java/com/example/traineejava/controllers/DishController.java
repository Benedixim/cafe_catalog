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
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class DishController {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CafeRepository cafeRepository;


    @GetMapping("/dishes")
    public String dishMain(Model model)
    {
        Iterable<Dish> dishes = dishRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("posts", dishes);//передаем в шаблон

        int dishCount = ((Collection<?>) dishes).size(); // получаем количество блюд
        model.addAttribute("dishCount", dishCount); // передаем количество блюд в шаблон

        return "dish/dish-main";
    }


    @GetMapping("/dishes/add")
    public String dishAdd(Model model)
    {
        Iterable<Category> categories = categoryRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("categories", categories);//передаем в шаблон
        return "dish/dish-add";
    }

    @PostMapping("/dishes/add")
    public String dishAdd(@RequestParam String name, @RequestParam String description, @RequestParam String link, @RequestParam float price, @RequestParam int weight, @RequestParam String category, Model model)
    {
        Category categoryObj = categoryRepository.findByName(category); // Получаем объект Category по имени
        Dish dish = new Dish(name, description, price, weight, link, categoryObj);
        dishRepository.save(dish);
        return "redirect:/dishes";
    }

    @GetMapping("/dishes/{idDish}")
    public String dishDetails(@PathVariable(value = "idDish") long idDish, Model model)
    {
        if(!dishRepository.existsById(idDish)){
            return "redirect:/dishes";
        }
        Optional<Dish> dish = dishRepository.findById(idDish);
        ArrayList<Dish> res = new ArrayList<>();
        dish.ifPresent(res::add);
        model.addAttribute("dish", res);
        return "dish/dish-details";
    }

    @GetMapping("/dishes/{idDish}/edit")
    public String dishEdit(@PathVariable(value = "idDish") long id, Model model)
    {


        if(!dishRepository.existsById(id)){
            return "redirect:/dishes";
        }


        Optional<Dish> dish = dishRepository.findById(id);
        ArrayList<Dish> res = new ArrayList<>();
        dish.ifPresent(res::add);
        model.addAttribute("dish", res);



        Iterable<Category> categories = categoryRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("categories", categories);//передаем в шаблон


        return "dish/dish-edit";
    }

    @PostMapping("/dishes/{idDish}/edit")
    public String dishUpdate(@PathVariable(value = "idDish") long id, @RequestParam String name, @RequestParam float price, @RequestParam int weight, @RequestParam String linkPhoto, @RequestParam String description, @RequestParam String categoryName, Model model)
    {
        Dish dish = dishRepository.findById(id).orElseThrow();
        dish.setName(name);
        dish.setDescription(description);
        dish.setWeight(weight);
        dish.setPrice(price);
        dish.setLinkPhoto(linkPhoto);

        Category category = categoryRepository.findByName(categoryName);
        dish.setCategory(category);

        dishRepository.save(dish);
        return "redirect:/dishes";
    }

    @PostMapping("/dishes/{id}/remove")
    public String dishDelete(@PathVariable(value = "id") long id, Model model)
    {
        Dish dish = dishRepository.findById(id).orElseThrow();
        dishRepository.delete(dish);

        Iterable<Cafe> cafes = cafeRepository.findAll(); //массив всех данных полученные из таблички Post

        for (Cafe cafe : cafes) {
            List<Category> categoriesToRemove = new ArrayList<>();
            List<Cafe> cafesToRemove = new ArrayList<>();

            for (Category category : cafe.getCategories()) {
                boolean hasDishes = false;

                for (Dish d : cafe.getDishes()) {
                    if (category == d.getCategory()) {
                        hasDishes = true;
                        break;
                    }
                }

                if (!hasDishes) {
                    category.getCafes().remove(cafe);
                    categoryRepository.save(category);
                    cafesToRemove.add(cafe);
                }


            }

            cafe.getCategories().removeAll(categoriesToRemove);
            cafeRepository.save(cafe);
        }

        return "redirect:/dishes";
    }
}
