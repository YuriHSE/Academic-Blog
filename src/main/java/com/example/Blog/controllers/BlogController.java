package com.example.Blog.controllers;

import com.example.Blog.models.Post;
import com.example.Blog.repository.PostRepository;
import com.example.Blog.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping("api/v1/apps")
@AllArgsConstructor //через конструктор загружаем экземпляр сервиса
public class BlogController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    // вывод всех постов блога
    @GetMapping("/blog")
    public String blogMain(Model model) {
        //передаем все посты в шаблон из БД
        Iterable<Post> posts = postService.findAllPosts();

        // передаем в модель массив постов
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    // добавление нового поста - переход на страничку заполнения формы
    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    // добавление нового поста - получение данных из формы и запись в Б
    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title,
                              @RequestParam String anons,
                              @RequestParam String full_text,
                              @AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam("file") MultipartFile file, Model model) throws IOException {
        postService.addPost(title, anons, full_text, userDetails.getUsername(), file);
        return "redirect:/api/v1/apps/blog";
    }


//    @PostMapping("/blog/add")
//    public String blogPostAdd(@RequestBody Post post, Model model) {
//        postService.addPost(post.getTitle(), post.getAnons(), post.getFull_text(), post.getAuthor());
//        return "redirect:/api/v1/apps/blog";
////        model.addAttribute("error", post.getAuthor());
////        return "test";
//    }


    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        ArrayList<Post> res = postService.postDetails(id);
        if (res == null) {
            return "redirect:/api/v1/apps/blog";
        }
        for (Post post : res) {
            post.setViews(post.getViews() + 1);
            postRepository.save(post);
        }
        model.addAttribute("post", res);

        return "blog-details";
    }


    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {
        ArrayList<Post> res = postService.postDetails(id);
        if (res == null) {
            return "redirect:/api/v1/apps/blog";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (Post post : res) {
            if (!post.getAuthor().equals(auth.getName())) {
                return "window"; //всплывающее окно(модальное)
            }
        }
        model.addAttribute("post", res);
        return "/blog-edit";
    }

    // обновление поста

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title,
                                 @RequestParam String anons,
                                 @RequestParam String full_text,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("file") MultipartFile file, Model model) throws IOException {

        postService.postUpdate(id, title, anons, full_text, userDetails.getUsername(), file);
        return "redirect:/api/v1/apps/blog";
    }


    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        if (!post.getAuthor().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return "window"; //всплывающее окно(модальное)
        }
        postService.postDelete(id);
        return "redirect:/api/v1/apps/blog";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter, Model model) {
        Iterable<Post> posts = postService.postFilter(filter);

        model.addAttribute("posts", posts);
        return "blog-main";
    }

}