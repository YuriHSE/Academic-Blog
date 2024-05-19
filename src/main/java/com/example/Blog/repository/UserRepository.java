package com.example.Blog.repository;

import com.example.Blog.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByName(String name);
    List<MyUser> findByNameStartsWithIgnoreCaseOrderByName(String prefix);
    MyUser findByActivationCode(String code);
}

