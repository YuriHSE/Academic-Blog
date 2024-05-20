package com.example.Blog.config;

import com.example.Blog.models.MyUser;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class MyUserDetails implements UserDetails {
    private MyUser user;
    public MyUserDetails(MyUser user) {
        this.user = user;
    } //принимаем пользователей

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(user.getRoles().split(", "))//здесь сплитим запись роли на отдельные кусочки
                .map(SimpleGrantedAuthority::new) //преобразуем строковое значение в нужный класс
                .collect(Collectors.toList()); //собираем все роли в список
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //указывает, включен пользователь или отключен
        return true; //включен
    }
}
