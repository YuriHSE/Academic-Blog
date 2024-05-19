package com.example.Blog.services;

import com.example.Blog.models.Application;
import com.example.Blog.models.MyUser;
import com.example.Blog.repository.UserRepository;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class AppService {
    //нужно запихнуть некоторое количество приложений в какую-то структуру данных, которую сейчас создадим
    private List<Application> applications;
    @Autowired
    private UserRepository repository; //для добавления в репозиторий пользователя
    private PasswordEncoder encoder; //для шифрования пароля, принимаемого от пользователя

    @Autowired
    private MailService mailService;
    @Autowired
    private UserRepository userRepository;

    //Пример с какой-то моделью Application
    @PostConstruct //гарантирует вызов метода один раз сразу после инициализации свойств компонентов
    public void loadAppInDB() {
        Faker faker = new Faker();
        applications = IntStream.rangeClosed(1, 100)//заполняем структуру 100 случайными экземплярами
                .mapToObj(i -> Application.builder()
                        .id(i)
                        .name(faker.app().name())
                        .author(faker.app().author())
                        .version(faker.app().version())
                        .build())
                .toList();
    }
    public List<Application> allApplications() {
        return applications;
    }
    public Application applicationByID(int id) {
        return applications.stream()
                .filter(app -> app.getId() == id)
                .findFirst()
                .orElse(null);
    }



    public void addUser(MyUser user) {
        user.setPassword(encoder.encode(user.getPassword())); //зашифровали пароль
        repository.save(user);

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Academic blog. Please, visit next link: http://localhost:8080/api/v1/apps/activate/%s",
                user.getName(),
                user.getActivationCode()
        );

        if (!StringUtils.isEmpty(user.getEmail())) {
            mailService.sendSimpleMail(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        MyUser user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        userRepository.save(user);

        return true;
    }

    public List<MyUser> allUsers() {
        return repository.findAll();
    }

    public List<MyUser> search(String prefix) {
        return repository.findByNameStartsWithIgnoreCaseOrderByName(prefix);
    }

}
