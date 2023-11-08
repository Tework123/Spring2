package springApp2.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import springApp2.models.User;
import springApp2.repositories.UserRepository;
import springApp2.services.UserService;

import java.io.IOException;

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
    public String profile(@PathVariable("id") Integer id, @AuthenticationPrincipal User currentUser, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого чела");
        }
        model.addAttribute("user", user);
        model.addAttribute("posts", user.getPosts());
        model.addAttribute("currentUser", currentUser);
        return "user/profileTemplate";
    }

    @GetMapping("/profile/edit")
    public String editProfileHtml(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "user/profileEditTemplate";
    }

    @PatchMapping("/profile/edit")
    public String editProfile(@Valid User editUser, @AuthenticationPrincipal User currentUser, @RequestParam("file") MultipartFile file, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "user/profileEditTemplate";
        }
        User savedUser = userService.editProfile(editUser, currentUser, file);
        userService.savePhotos(file, savedUser);
        return "redirect:/post";
    }


    @GetMapping("/hello")
    public String securityUrl() {
        return "user/helloTemplate";
    }
}
