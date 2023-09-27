package com.example.traineejava.controllers;

import com.example.traineejava.models.Cafe;
import com.example.traineejava.models.Category;
import com.example.traineejava.models.Dish;
import com.example.traineejava.repo.CafeRepository;
import com.example.traineejava.repo.CategoryRepository;
import com.example.traineejava.repo.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/cafes")
public class CafeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private DishRepository dishRepository;


    @GetMapping("")
    public String cafeMain(@RequestParam(required = false) String name, @RequestParam(required = false) String f, @RequestParam(required = false) String s, Model model)
    {
        Iterable<Cafe> cafes;
        if(name != null && !name.isEmpty()){
            if(f != null && s != null){
                cafes = cafeRepository.findByNameContainingIgnoreCaseAndRatingBetween(name, Float.parseFloat(f), Float.parseFloat(s));
            }
            else{
                cafes = cafeRepository.findByNameContainingIgnoreCase(name);
            }
        }
        else if(f != null && s != null){
            cafes = cafeRepository.findByRatingBetween(Float.parseFloat(f),Float.parseFloat(s));
        }
        else {
            cafes = cafeRepository.findAll(); //массив всех данных полученные из таблички Post
        }
        model.addAttribute("posts", cafes);//передаем в шаблон
        return "cafe/cafe-main";
    }

    @GetMapping("/add")
    public String cafeAdd(Model model)
    {
        return "cafe/cafe-add";
    }

    @PostMapping(value = "/add")
    public String cafePostAdd(@RequestParam String description, @RequestParam String link, @RequestParam String name, @RequestParam String address, Model model)
    {
        Cafe cafe = new Cafe( description, link, name, address);
        cafeRepository.save(cafe);
        return "redirect:/cafes";
    }

    @GetMapping("/{idCafe}")
    public String cafeDetails(@PathVariable(value = "idCafe") long idCafe, Model model)
    {
        if(!cafeRepository.existsById(idCafe)){
            return "redirect:/cafes";
        }
        Optional<Cafe> cafeOptional = cafeRepository.findById(idCafe);
        //Iterable<Dish> dishesToDelete;
        if (cafeOptional.isPresent()) {
            Cafe cafe = cafeOptional.get();
            cafe.oneMore(); // увеличиваем число просмотров на 1
            cafeRepository.save(cafe); // сохраняем изменения в репозитории


            Iterable<Dish> dishesHave = cafe.getDishes();
            model.addAttribute("dishesHave", dishesHave);//передаем в шаблон

            List<Category> categoryList = new ArrayList<>();
            Category c = new Category();
            for (Dish d: dishesHave){
                c = dishRepository.findByName(d.getName()).getCategory();
                if (!categoryList.contains(c)) categoryList.add(c);
             }
            model.addAttribute("categories", categoryList);

            model.addAttribute("cafe", cafe);
        }
        return "cafe/cafe-details";
    }

    @GetMapping("/{idCafe}/edit")
    public String cafeEdit(@PathVariable(value = "idCafe") long id, Model model)
    {

        if(!cafeRepository.existsById(id)){
            return "redirect:/cafes";
        }


        Optional<Cafe> cafe = cafeRepository.findById(id);
        ArrayList<Cafe> res = new ArrayList<>();
        cafe.ifPresent(res::add);
        model.addAttribute("cafe", res);


        List<Dish> dishesHave = cafe.get().getDishes();
        model.addAttribute("dishesHave", dishesHave);//передаем в шаблон


        Iterable<Dish> dishes = dishRepository.findAll(); //массив всех данных полученные из таблички Post
        List<Dish> dishesAdd = new ArrayList<>();
        for (Dish d: dishes) {
            if(!dishesHave.contains(d)) dishesAdd.add(d);
        }
        model.addAttribute("dishesAdd", dishesAdd);//передаем в шаблон






        return "cafe/cafe-edit";
    }

    @PostMapping("/{idCafe}/edit")
    public String cafeUpdate(@PathVariable(value = "idCafe") long id, @RequestParam(required = false) String name, @RequestParam(required = false) String address, @RequestParam(required = false) float rating, @RequestParam(required = false) String linkPhoto, @RequestParam(required = false) String description, @RequestParam(required = false) List<String> dishesAdd, @RequestParam(required = false) List<String> dishesDel, @RequestParam(required = false) List<String> categoriesDel, Model model)
    {
        Cafe cafe = cafeRepository.findById(id).orElseThrow();
        cafe.setName(name);
        cafe.setRating(rating);
        cafe.setDescription(description);
        cafe.setAddress(address);
        cafe.setLinkPhoto(linkPhoto);


       // List<Category> selectedCategories = new ArrayList<>();
        if(dishesAdd != null){
        for (String dishName : dishesAdd) {

            Dish dish = dishRepository.findByName(dishName);
            cafe.addDish(dish);
//            cafe.addCategory(dish.getCategory());


        }}

        if(dishesDel != null){
        for (String dishName: dishesDel){
            if(dishName==null) break;
            Dish dish = dishRepository.findByName(dishName);
            cafe.delDish(dish);

        }}

        cafe.updateMoney();

//        if(categoriesDel != null){
//        for (String categoryName: categoriesDel ){
//            if(categoryName==null) break;
//            Category category = categoryRepository.findByName(categoryName);
//            category.deleteCafe(cafe);
//            categoryRepository.save(category);
//
//            cafe.delCategory(category);
//
//        }}

      //  cafe.setCategories(selectedCategories);



        cafeRepository.save(cafe);
        return "redirect:/cafes/{idCafe}";
    }

    @PostMapping("/{id}/remove")
    public String cafeDelete(@PathVariable(value = "id") long id, Model model)
    {
        Cafe cafe = cafeRepository.findById(id).orElseThrow();


//        Iterable<Category> categories = categoryRepository.findAll(); //массив всех данных полученные из таблички Post
//        for (Category category : categories) {
//            boolean hasCafesInCategory = false;
//            for (Cafe caf : category.getCafes()) {
//                    if (cafe == caf) {
//                        hasCafesInCategory = true;
//                        break;
//                    }
//            }
//            if (hasCafesInCategory) {
//                category.getCafes().remove(cafe);
//            }
//            categoryRepository.save(category);
//        }

        cafeRepository.delete(cafe);

        return "redirect:/cafes";
    }
}
