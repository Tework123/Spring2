package springApp2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import springApp2.models.User;
import springApp2.services.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/registration")
    public String register() {
        System.out.println("444444444444444");

        return "user/register";
    }

    @PostMapping("/registration")
    public String createUser(User user) {
//        не видит эту функцию
        System.out.println("3333333333333333");

        userService.createUser(user);
        return "redirect:/user/login";
    }

    @GetMapping("/hello")
    public String securityUrl() {
        return "user/hello";
    }
}
