package com.example.Blog.repository;

import com.example.Blog.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    //List<Post> findByAuthor(String author);
    List<Post> findByTitleContainingIgnoreCase(String title);
}
