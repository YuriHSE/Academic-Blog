package com.example.Blog.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity //модель(создает табличку)
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //позволит каждый раз генерировать новое значение внутри этого поля
    private Long id;

    private String title, anons, full_text;

//    @ManyToOne(fetch = FetchType.EAGER, optional = false) //указываем бд, что у нас в этой связи одному пользователю соответствует множество сообщений (EAGER - получаем информацию об авторе вместе с сообщением сразу же)
//    @JoinColumn(name = "user_id")
//    private MyUser author;
    private String author;
    private String filename;
    private int views;



    public Post() { //в любой модели должен быть пустой конструктор
    }
    public Post(String title, String anons, String full_text, String author) {
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
        this.author = author;
    }



    public String getAuthorName() {
        return author != null ? author: "<none>";
    }

}
