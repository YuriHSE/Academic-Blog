package com.example.Blog.controllers;

import com.example.Blog.models.Application;
import com.example.Blog.models.MyUser;
import com.example.Blog.repository.UserRepository;
import com.example.Blog.services.AppService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //чтобы сиреализовать все ответы в json
//@Controller
@RequestMapping("api/v1/apps/admin")
@AllArgsConstructor //через конструктор загружаем экземпляр сервиса
public class AdminController {
    private final UserRepository userRepository;
    private AppService service; //создаем экземпляр сервиса
    //private MyUserDetailsService myUserDetailsService;
    //приступим к созданию контрольных точек

    //вторая контрольная точка
    //@ResponseBody
    @GetMapping("/all-applications")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')") //с помощью этой аннотации мы настраиваем правила, разрешающие доступ к этой контрольной точке(можно добавлять через &&)
    public List<Application> allApplications() {
        return service.allApplications();
    }

    //третья контрольная точка
    //@ResponseBody
    @GetMapping("/{id}")
    public Application applicationByID(@PathVariable int id) {
        return service.applicationByID(id);
    }

    //@ResponseBody
    @PostMapping("/new-user")
    public String addUser(@RequestBody MyUser user) {
        service.addUser(user);
        return "User is saved";
    }


    //@ResponseBody
    @GetMapping("/all-users")
    public List<MyUser> allUsers() {
        return service.allUsers();
    }

    //@ResponseBody
    @GetMapping("/name")
    public List<MyUser> findByName(@RequestParam("prefix") String prefix) {
        return service.search(prefix);
    }
    //url: localhost:8080/api/v1/apps/name?prefix=имя

    //можно добавить функцию изменения пользователей(хотя можно и в бд)
}
