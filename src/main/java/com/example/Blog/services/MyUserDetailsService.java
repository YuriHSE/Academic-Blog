package com.example.Blog.services;

import com.example.Blog.config.MyUserDetails;
import com.example.Blog.models.MyUser;
import com.example.Blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired //внедрим репозиторий полем
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //возвращает userdetails, подходящего класса нет, а значитнеобхолимо сдеалаьь свою реализацию данного интерейса(ьггыуквуефшды)
        Optional<MyUser> user = repository.findByName(username);
        return user.map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

}

