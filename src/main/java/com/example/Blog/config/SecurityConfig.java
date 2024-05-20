package com.example.Blog.config;

import com.example.Blog.services.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //покажем что этот класс является конфигурационный бином
@EnableWebSecurity //эта аннотация покажет, что весь класс будет применен глобальной веб безопасности
@EnableMethodSecurity //чтобы моделирование авторизации на уровне метода работало
public class SecurityConfig {

    //первый метод создаст пользователя и сохранит в памяти приложения
    @Bean
    //чтобы encoder автоматически внедрился в конструктор, сам метод должен находится в контексте приложения, то есть у него должна быть эта аннотация
    public UserDetailsService userDetailsService() { //UserDetailsService - интерфейс, позволяющий собирать сведения о пользователе в контексте безопасности
        //PasswordEncoder - это интерфейс, использующийся для выполнения одностороннего преобразования пароля(обычно используется для сравнивания пароля с введенным пользователем)(здесь в аргументе уже не нужен, так как используем в провайдере)
        //UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).roles("ADMIN").build(); //используем этот интерфейс для предоставления информации о пользователе, включая и имя. и пароль, и статус учетной записи, роли и т д
        //пароль обязательно шифруем(encoder), так как в бд будет храниться зашифрованный

        //UserDetails user = User.builder().username("user").password(encoder.encode("user")).roles("USER").build();
        //UserDetails misha32 = User.builder().username("misha32").password(encoder.encode("misha32")).roles("ADMIN", "USER").build();
        //return new InMemoryUserDetailsManager(admin, user, misha32); //для сохранения пользователей в памяти
        return new MyUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {//HttpSecurity - позволяет конфигурировать аутентификацию и авторизацию http запросов, для того чтобы создать свой фильтр или цепочку фильтров используется интерфейс securityfolterchaim(для того. чтобы можно было натсроить доступы к старницам)
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/apps/blog/add", "api/v1/apps/blog/{id}/edit", "api/v1/apps/blog/{id}/remove").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/apps/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()) //две звездочки означают все адреса после введенного до них шаблона
                //.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)

                .formLogin(form -> form.loginPage("/login").permitAll())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() { //провайдер, реализующий аутенфикацию(механизм, с помощью котрого вызывающий абонент, доказывают, что они действуют от именни конкретных пользователей) //используется для подтверждения личности пользователя
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); //DaoAuthenticationProvider - реализация провайдера, которая реализует UserDetailService and PasswordEncoder для аутентификации имени пользователя и пароля
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//вернет этот метод один из самых популярных кодировщиков
    }
}

