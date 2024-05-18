package com.example.Blog.services;

import com.example.Blog.models.Post;
import com.example.Blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
//@AllArgsConstructor
@Component
public class PostService {
    @Autowired
    private PostRepository postRepository;

    //@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Value("${upload.path}") //указываем Spring, что хотим получить переменную
    private String uploadPath;
    public PostService(@Value("${upload.path}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public PostService() {
    }

    public Iterable<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public void addPost(String title, String anons, String full_text, String author, MultipartFile file) throws IOException {
        Post post = new Post(title, anons, full_text, author);
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); //если не существует, то создаем
            }

            String uuidFile = UUID.randomUUID().toString(); //уникальное имя файла
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            post.setFilename(resultFilename);
        }
        postRepository.save(post);
    }

    public ArrayList<Post> postDetails(long id) {
        if(!postRepository.existsById(id)){
            return null;
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        return res;
    }

    public void postUpdate(long id, String title, String anons, String full_text, String author, MultipartFile file) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        post.setAuthor(author);
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); //если не существует, то создаем
            }

            String uuidFile = UUID.randomUUID().toString(); //уникальное имя файла
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            post.setFilename(resultFilename);
        }
        postRepository.save(post);
    }

    public void postDelete(long id) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
    }

    public Iterable<Post> postFilter(String filter) {
        Iterable<Post> posts;
        if (filter != null && !filter.isEmpty()) {
            posts = postRepository.findByTitleContainingIgnoreCase(filter);
        } else {
            posts = postRepository.findAll();
        }
        return posts;
    }

}
