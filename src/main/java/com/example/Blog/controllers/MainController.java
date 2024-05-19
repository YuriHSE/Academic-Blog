package com.example.Blog.controllers;

import com.example.Blog.models.Post;
import com.example.Blog.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("api/v1/apps")
@AllArgsConstructor
public class MainController {
    @Autowired
    private PostService postService;

    @GetMapping("/home")
    public String home(Model model) {

        model.addAttribute("title", "Главная страница");
        //передаем все посты в шаблон из БД
        Iterable<Post> posts = postService.findAllPosts();

        // передаем в модель массив постов
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "О нас");
        return "about";
    }

}