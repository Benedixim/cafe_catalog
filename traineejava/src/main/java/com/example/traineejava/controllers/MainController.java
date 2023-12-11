package com.example.traineejava.controllers;

import com.example.traineejava.models.*;
import com.example.traineejava.repo.*;
import com.example.traineejava.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @GetMapping("/user/icon")
    public ResponseEntity<Resource> getProfile() {

        String email = userService.getCurrentUser();

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            //throw new UsernameNotFoundException("No user found with email");
            email = "https://cdn2.iconfinder.com/data/icons/social-media-flat-line/70/user-1024.png";
        }
        else {
            email = user.getProfilePhotoUrl();
        }


        Resource iconResource = null;
        try {
            iconResource = new UrlResource(email);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Убедитесь, что ресурс существует и доступен
        /*if (!iconResource.exists() || !iconResource.isReadable()) {
            throw new RuntimeException("Unable to load user icon");
        }*/

        // Создайте HTTP-ответ с изображением пользователя
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Задайте соответствующий тип содержимого

        return new ResponseEntity<>(iconResource, headers, HttpStatus.OK);
    }


    private List<Cafe> cafes;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private DishRepository dishRepository;

    @GetMapping("/")
    public String Main(Model model) {
        return "main/main";
    }

    @GetMapping("/search")
    public String Search(Model model) {
        Iterable<Category> categories = categoryRepository.findAll(); //массив всех данных полученные из таблички Post
        model.addAttribute("categories", categories);//передаем в шаблон
        System.out.println("Получили" + cafes);
        model.addAttribute("posts", (Iterable<Cafe>) cafes);//передаем в шаблон
        cafes = null;
        return "main/search";
    }

    @PostMapping("/search")
    public String searchAdd(@RequestParam(required = false) String categoryChoice,
                            @RequestParam(required = false) String categoryChoice2,
                            @RequestParam(required = false) String categoryChoice3,
                            @RequestParam(required = false) String categoryChoice4,
                            @RequestParam(required = false) String time1,
                            @RequestParam(required = false) String time2,
                            @RequestParam(required = false) int money,
                            Model model) {
      //  System.out.println(categoryChoice + categoryChoice2 + categoryChoice3 + categoryChoice4 + time1 + time2 + money);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date1, date2;
        date1 = date2 = null;
        try {
            date1 = sdf.parse(time1);
            date2 = sdf.parse(time2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Time time_first = new Time(date1.getTime());
        Time time_second = new Time(date2.getTime());

        cafes = cafeRepository.findByOpenBeforeAndCloseAfterAndCloseAfterOrderByMoneyDesc(time_first, time_second, time_first);
       // System.out.println(cafes);

        List<Dish> dishList = new ArrayList<>();
        Category c = new Category();

        if (categoryChoice != null) {
            c = categoryRepository.findByName(categoryChoice);
            dishList = dishRepository.findByCategory(c);

            cafes.removeAll(Cafe.cafesToRemove(cafes, dishList));
          //  System.out.println("Результат 1: " + cafes);

            //cafes = Cafe.returnCommon((List<Cafe>) cafes, (List<Cafe>) cafeRepository.findByDishesContains(dishList));
        }//ищет по наличию всех блюд из данной категории, а надо чтобы хотя бы одно

        if (categoryChoice2 != null) {
            c = categoryRepository.findByName(categoryChoice2);
            dishList = dishRepository.findByCategory(c);

            cafes.removeAll(Cafe.cafesToRemove(cafes, dishList));
            //System.out.println("Результат 2: " + cafes);
        }

        if (categoryChoice3 != null) {
            c = categoryRepository.findByName(categoryChoice3);
            dishList = dishRepository.findByCategory(c);

            cafes.removeAll(Cafe.cafesToRemove(cafes, dishList));
          //  System.out.println("Результат 3: " + cafes);
        }

        if (categoryChoice4 != null) {
            c = categoryRepository.findByName(categoryChoice4);
            dishList.addAll(dishRepository.findByCategory(c));

            cafes.removeAll(Cafe.cafesToRemove(cafes, dishList));
           // System.out.println("Результат 4: " + cafes);
        }

        if (money != 0) {
            int min = 0, max = 9999999;
            switch (money) {
                case 1:
                    min = 1;
                    max = 5;
                    break;
                case 2:
                    min = 5;
                    max = 15;
                    break;
                case 3:
                    min = 15;
                    max = 30;
                    break;
                case 4:
                    min = 30;
                    max = 60;
                    break;
                case 5:
                    min = 60;
                    max = 99999999;
            }
            List<Cafe> cafeList = cafeRepository.findByMoneyBetween(min,max);
            cafes.retainAll(cafeList);
            //System.out.println("Результат 5: " + cafes);
        }

       // System.out.println("Передали" + cafes);

        //System.out.println(cafes);

        //Iterable<Cafe> cafes = cafeRepository.
        // System.out.println(categoryChoice+categoryChoice2+categoryChoice3+categoryChoice4+time1+time2+money);
        //model.addAttribute("posts", cafes);//передаем в шаблон
        return "redirect:/search";
    }


    private final AuthenticationManager authenticationManager;

    public MainController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String login() {
        return "main/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "main/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String createNewUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("user") User user) {

        try {

            user.setRole("USER");

            User newUser = userService.createUser(user);
            if (newUser == null) {
                return "redirect:/register?error";
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            return "redirect:/";

        } catch (Exception e) {
            return "redirect:/register?error";
        }

    }

    @GetMapping("/profile")
    public String register(Model model) {

        String email = userService.getCurrentUser();

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            //throw new UsernameNotFoundException("No user found with email");
            return "redirect:/login";
        }

        List<Comment> comments = commentRepository.findByUser(user);
        model.addAttribute("comments", comments);

        if(user.getRole().equals("ADMIN")){
            List<User> users = userRepository.findAll();
            model.addAttribute("users", users);
        }


        model.addAttribute("user", user);
        return "main/profile";
    }





}
