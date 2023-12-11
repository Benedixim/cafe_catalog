package com.example.traineejava.controllers;

import com.example.traineejava.models.*;
import com.example.traineejava.repo.*;
import com.example.traineejava.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/cafes")
public class CafeController {

    private CategoryRepository categoryRepository;

    private CafeRepository cafeRepository;

    private DishRepository dishRepository;

    private UserService userService;

    private UserRepository userRepository;

    private CommentRepository commentRepository;

    private RatingRepository ratingRepository;

    public CafeController(CategoryRepository categoryRepository,
                          CafeRepository cafeRepository,
                          DishRepository dishRepository,
                          UserService userService,
                          UserRepository userRepository, CommentRepository commentRepository, RatingRepository ratingRepository) {
        this.categoryRepository = categoryRepository;
        this.cafeRepository = cafeRepository;
        this.dishRepository = dishRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.ratingRepository = ratingRepository;
    }

    @GetMapping
    public String cafeMain(@RequestParam(required = false) String name, @RequestParam(required = false) String f, @RequestParam(required = false) String s, Model model)
    {
        if (f == null) f = "0.0";
        if (s == null) s = "5.0";
        if(f.isEmpty()) f = "0.0";
        if(s.isEmpty()) s = "5.0";
        Iterable<Cafe> cafes;
        if(name != null && !name.isEmpty()){
            if(!f.equals("") || !s.equals("")){
                //cafes = cafeRepository.findByNameContainingIgnoreCaseAndRatingBetween(name, Float.parseFloat(f), Float.parseFloat(s));
                cafes = cafeRepository.findByNameContainingIgnoreCase(name);
                Iterator<Cafe> iterator = cafes.iterator();
                while (iterator.hasNext()) {
                    Cafe cafe = iterator.next();
                    if ((cafe.getRating() < Float.parseFloat(f)) || (cafe.getRating() > Float.parseFloat(s))) {
                        iterator.remove();
                    }
                }
            }
            else{
                cafes = cafeRepository.findByNameContainingIgnoreCase(name);
            }
        }
        else if(f != null && s != null){
            //Iterable<Comment> comments = commentRepository.findByCafe();
            cafes = cafeRepository.findAll();
            Iterator<Cafe> iterator = cafes.iterator();
            while (iterator.hasNext()) {
                Cafe cafe = iterator.next();
                if ((cafe.getRating() < Float.parseFloat(f)) || (cafe.getRating() > Float.parseFloat(s))) {
                    iterator.remove();
                }
            }



//            Rating rate1 = new Rating(Float.parseFloat(f));
//            Rating rate2 = new Rating(Float.parseFloat(s));
//            cafes = cafeRepository.findByRatingBetween(rate1,rate2);
            //cafes = cafeRepository.findByRatingBetween(Float.parseFloat(f),Float.parseFloat(s));
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
        Rating rating = new Rating();
        cafe.initRating(rating);
        ratingRepository.save(rating);
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


            Iterable<Comment> comments = commentRepository.findByCafe(cafeOptional.get());
            model.addAttribute("comments", comments);



        }
        return "cafe/cafe-details";
    }

    @PostMapping("/{idCafe}")
    public String commentAdd(@PathVariable(value = "idCafe") long idCafe, @RequestParam(required = false) String description, @RequestParam(required = false) float atmosphere, @RequestParam(required = false) float cookery, @RequestParam(required = false) float price, @RequestParam(required = false) float service, @RequestParam(required = false) float staff) {


        Cafe cafe = cafeRepository.findById(idCafe).orElseThrow();

        String email = userService.getCurrentUser();
        User user = userRepository.findUserByEmail(email);
        if(user==null){
            user = userRepository.findUserByEmail("noname");
        }
        float rate = (service+staff+cookery+atmosphere+price) / 5;
        Rating rating = new Rating(rate,staff, atmosphere, service, price, cookery);
        ratingRepository.save(rating);


        Iterable<Comment> comments = commentRepository.findByCafe(cafe);
        int count = 0;
        for(Comment c: comments){
            count++;
        }
        cafe.setRating(rating, count);
        cafeRepository.save(cafe);

        Comment com = new Comment(rating, new Date(), description, user, cafe);
        commentRepository.save(com);

        return "redirect:/cafes/{idCafe}";
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
       // cafe.setRating(rating);
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
