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
        return "user/loginTemplate";
    }

    @GetMapping("/registration")
    public String register() {
        return "user/registerTemplate";
    }

    @PostMapping("/registration")
    public String createUser(User user) {
        userService.createUser(user);
        return "redirect:/user/loginTemplate";
    }

    @GetMapping("/hello")
    public String securityUrl() {
        return "user/helloTemplate";
    }
}
