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
public class CafeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private DishRepository dishRepository;


    @GetMapping("/")
    public String Main(Model model)
    {
        Iterable<Cafe> cafes = cafeRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("posts", cafes);//передаем в шаблон
        return "cafe/cafe-main";
    }


    @GetMapping("/cafes")
    public String cafeMain(Model model)
    {

        Iterable<Cafe> cafes = cafeRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("posts", cafes);//передаем в шаблон
        return "cafe/cafe-main";
    }

    @GetMapping("/cafes/add")
    public String cafeAdd(Model model)
    {
        return "cafe/cafe-add";
    }

    @PostMapping("/cafes/add")
    public String cafePostAdd(@RequestParam String name, @RequestParam String description, @RequestParam String link, @RequestParam String address, Model model)
    {
        Cafe cafe = new Cafe( description, link, name, address);
        cafeRepository.save(cafe);
        return "redirect:/cafes";
    }

    @GetMapping("/cafes/{idCafe}")
    public String cafeDetails(@PathVariable(value = "idCafe") long idCafe, Model model)
    {
        if(!cafeRepository.existsById(idCafe)){
            return "redirect:/cafes";
        }
        Optional<Cafe> cafeOptional = cafeRepository.findById(idCafe);
        if (cafeOptional.isPresent()) {
            Cafe cafe = cafeOptional.get();
            cafe.oneMore(); // увеличиваем число просмотров на 1
            cafeRepository.save(cafe); // сохраняем изменения в репозитории
            model.addAttribute("cafe", cafe);
        }
        return "cafe/cafe-details";
    }

    @GetMapping("/cafes/{idCafe}/edit")
    public String cafeEdit(@PathVariable(value = "idCafe") long id, Model model)
    {

        if(!cafeRepository.existsById(id)){
            return "redirect:/cafes";
        }


        Optional<Cafe> cafe = cafeRepository.findById(id);
        ArrayList<Cafe> res = new ArrayList<>();
        cafe.ifPresent(res::add);
        model.addAttribute("cafe", res);


        Iterable<Dish> dishes = dishRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("dishes", dishes);//передаем в шаблон


        return "cafe/cafe-edit";
    }

    @PostMapping("/cafes/{idCafe}/edit")
    public String cafeUpdate(@PathVariable(value = "idCafe") long id, @RequestParam String name, @RequestParam String address, @RequestParam float rating, @RequestParam String linkPhoto, @RequestParam String description, @RequestParam List<String> dishesAdd, @RequestParam List<String> dishesDel, @RequestParam List<String> categoriesDel, Model model)
    {
        Cafe cafe = cafeRepository.findById(id).orElseThrow();
        cafe.setName(name);
        cafe.setRating(rating);
        cafe.setDescription(description);
        cafe.setAddress(address);
        cafe.setLinkPhoto(linkPhoto);


       // List<Category> selectedCategories = new ArrayList<>();

        for (String dishName : dishesAdd) {
            if(dishName==null) break;
            Dish dish = dishRepository.findByName(dishName);
            dish.addCafe(cafe);
            dishRepository.save(dish);

            Category category = categoryRepository.findByName(dish.getCategory().getName());
            category.addCafe(cafe);
            categoryRepository.save(category);

            cafe.addDish(dish);
            cafe.addCategory(dish.getCategory());


        }


        for (String dishName: dishesDel){
            if(dishName==null) break;
            Dish dish = dishRepository.findByName(dishName);
            dish.delCafe(cafe);
            dishRepository.save(dish);

            cafe.delDish(dish);

        }

        for (String categoryName: categoriesDel ){
            if(categoryName==null) break;
            Category category = categoryRepository.findByName(categoryName);
            category.deleteCafe(cafe);
            categoryRepository.save(category);

            cafe.delCategory(category);

        }

      //  cafe.setCategories(selectedCategories);



        cafeRepository.save(cafe);
        return "redirect:/cafes/{idCafe}";
    }

    @PostMapping("/cafes/{id}/remove")
    public String cafeDelete(@PathVariable(value = "id") long id, Model model)
    {
        Cafe cafe = cafeRepository.findById(id).orElseThrow();


        Iterable<Category> categories = categoryRepository.findAll(); //массив всех данных полученные из таблички Post
        for (Category category : categories) {
            boolean hasCafesInCategory = false;
            for (Cafe caf : category.getCafes()) {
                    if (cafe == caf) {
                        hasCafesInCategory = true;
                        break;
                    }
            }
            if (hasCafesInCategory) {
                category.getCafes().remove(cafe);
            }
            categoryRepository.save(category);
        }

        Iterable<Dish> dishes = dishRepository.findAll(); //массив всех данных полученные из таблички Post
        for (Dish dish : dishes) {
            boolean hasCafesInDishes = false;
            for (Cafe caf : dish.getCafes()) {
                if (cafe == caf) {
                    hasCafesInDishes = true;
                    break;
                }
            }
            if (hasCafesInDishes) {
                dish.getCafes().remove(cafe);
            }
            dishRepository.save(dish);
        }

        cafeRepository.delete(cafe);

        return "redirect:/cafes";
    }
}
