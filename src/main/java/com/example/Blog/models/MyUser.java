package com.example.Blog.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class MyUser{ //нельзя имплементировать userDetails, так как он уже есть в MyUserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) //name должен быть уникален
    private String name;
    private String password;
    private String roles;

//    @OneToMany(mappedBy = "author")
//    //@JoinColumn(name = "user_id")
//    private Set<Post> posts;
}
