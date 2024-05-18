package com.example.Blog.controllers;

import com.example.Blog.models.MyUser;
import com.example.Blog.repository.UserRepository;
import com.example.Blog.services.AppService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
//скорее всего ерунда(лучше рассмотерть работу json с html)
//но здесь у нас обычный контроллер
@Controller
@RequestMapping("api/v1/apps")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    private AppService appService;

//    public RegistrationController(AppService appService) {
//        this.appService = appService;
//    } здесь вместо этого мы использовали анатацию @AllArgsConstructor

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(MyUser user, Map<String, Object> model) {
        Optional<MyUser> userFromDb = userRepository.findByName(user.getName());

        if (userFromDb.isPresent()) {
            model.put("message", "User exists!");
            return "registration"; //потом добавим страницу ошибки
        }
        user.setRoles("ROLE_USER");
        appService.addUser(user);
//        model.put("error", user.getName() + ' ' + user.getPassword() + ' ' + user.getRoles() + ' ' + user.getId());
//        return "test";

        return "redirect:/login";
    }
}
