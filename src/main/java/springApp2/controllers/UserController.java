package springApp2.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springApp2.models.User;
import springApp2.repositories.UserRepository;
import springApp2.services.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "user/loginTemplate";
    }

    @GetMapping("/registration")
    public String register(User user) {
        return "user/registerTemplate";
    }

    @PostMapping("/registration")
    public String createUser(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/registerTemplate";
        }
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable("id") Integer id, Principal principal, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        model.addAttribute("posts", user.getPosts());

        return "user/profileTemplate";
    }

    @GetMapping("/profile/edit")
    public String editProfileHtml(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user/profileEditTemplate";
    }

    @PatchMapping("/profile/edit")
    public String editProfile(@Valid User editUser,
                              @AuthenticationPrincipal User oldUser,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "user/profileEditTemplate";
        }

        userService.editProfile(editUser, oldUser);
        return "redirect:/post";
    }

    //    в юзере делаем фото для аватара, только одно за раз можно грузить
    //    А для постов сразу несколько через list
    @GetMapping("/hello")
    public String securityUrl() {
        return "user/helloTemplate";
    }
}
