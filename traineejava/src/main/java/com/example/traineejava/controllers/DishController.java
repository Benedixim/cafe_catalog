package com.example.traineejava.controllers;

import com.example.traineejava.models.*;
import com.example.traineejava.models.recipecommands.GetAllRecipeCommand;
import com.example.traineejava.models.recipecommands.GetByDishRecipeCommand;
import com.example.traineejava.repo.*;
import com.example.traineejava.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class DishController {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    private final RecipeService recipeService;

    public DishController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @GetMapping("/dishes")
    public String dishMain(Model model)
    {
        Iterable<Dish> dishes = dishRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("posts", dishes);//передаем в шаблон

        int dishCount = ((Collection<?>) dishes).size(); // получаем количество блюд
        model.addAttribute("dishCount", dishCount); // передаем количество блюд в шаблон

        //String data = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];

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

        Iterable<Cafe> cafes = cafeRepository.findByDishesContains(dish.get()); //массив всех данных полученные из таблички Post
        model.addAttribute("cafes", cafes);//передаем в шаблон

        Iterable<Price> pricesList = priceRepository.findByDishOrderByDate(dish.get()); //массив всех данных полученные из таблички Post
        List<Float> prices = new ArrayList<>();
        List<Float> dates = new ArrayList<>();

        for (Price price: pricesList){
            prices.add(price.getPrice());

            //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            dates.add(Float.valueOf(price.getDate().getMonth()+1) + Float.valueOf((float) ((price.getDate().getYear() + 1900)*0.0001)));
            //dates.set(price.getDate().getYear() + 1900);
            //dates.add(String.valueOf(price.getDate()));

        }

        List<Float> calories = new ArrayList<>();
        calories.add(dish.get().getFat());
        calories.add(dish.get().getCarbon());
        calories.add(dish.get().getProtein());

        model.addAttribute("prices", prices);//передаем в шаблон
        model.addAttribute("dates", dates);//передаем в шаблон
        model.addAttribute("calories", calories);//передаем в шаблон

//        // Создание объекта RestTemplate
//        RestTemplate restTemplate = new RestTemplate();
//
//        // URL для POST-запроса
//        String url = "http://localhost:3031/save-recipe"; // Замените на нужный URL
//
//        // Параметры запроса
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("query", dish.map(Dish::getName).orElse("Default Name")); // Замените на нужные параметры запроса
//
//        // Заголовки запроса
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // Создание объекта запроса с параметрами и заголовками
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
//
//        // Выполнение POST-запроса
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        GetByDishRecipeCommand getByDishRecipeCommand = new GetByDishRecipeCommand(recipeRepository, dish.get());
        List<Recipe> recipes = recipeService.getByDishRecipes(getByDishRecipeCommand);
        model.addAttribute("recipes", recipes);

        return "dish/dish-details";
    }

    @PostMapping("/dishes/{idDish}")
    public String addPrice(@PathVariable(value = "idDish") long idDish, @RequestParam float price, @RequestParam String date, Model model){


        Optional<Dish> dish = dishRepository.findById(idDish);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = dateFormat.parse(date);
           
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
        Price pr = new Price();
        pr.setPrice(price);
        pr.setDish(dish.get());
        pr.setDate(date1);


        priceRepository.save(pr);


        return "redirect:/dishes/{idDish}";

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

        Iterable<Cafe> cafes = cafeRepository.findByDishesContains(dish.get()); //массив всех данных полученные из таблички Post
        model.addAttribute("cafes", cafes);//передаем в шаблон


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


        List<Cafe> cafesToRemove = cafeRepository.findByDishesContains(dish);
        for (Cafe cafe: cafesToRemove){
            cafe.getDishes().remove(dish);
            cafeRepository.save(cafe);
        }

        List<Recipe> recipesToRemove = recipeRepository.findByTitle(dish);
        for (Recipe recipe: recipesToRemove){

            recipeRepository.delete(recipe);
        }

        dishRepository.delete(dish);

        return "redirect:/dishes";
    }
}
