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
import springApp2.models.Follower;
import springApp2.models.User;
import springApp2.repositories.UserRepository;
import springApp2.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public String profile(@PathVariable("id") Integer id,
                          @AuthenticationPrincipal User currentUser,
                          Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого чела");
        }

//      для смены кнопки follow на unfollow и наоборот
        List<Follower> authors = userService.getAuthors(currentUser.getId());
        User author = null;
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).getUserAuthor().getId() == user.getId()) {
                author = user;
                break;
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("posts", user.getPosts());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("author", author);
        return "user/profileTemplate";
    }

    @GetMapping("/profile/edit")
    public String editProfileHtml(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "user/profileEditTemplate";
    }

    @PatchMapping("/profile/edit")
    public String editProfile(@Valid User editUser,
                              @AuthenticationPrincipal User currentUser,
                              @RequestParam("file") MultipartFile file,
                              BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "user/profileEditTemplate";
        }
        User savedUser = userService.editProfile(editUser, currentUser, file);
        userService.savePhotos(file, savedUser);
        return "redirect:/post";
    }

    //  FOLLOWERS

    @GetMapping("/profile/{id}/author")
    public String getAuthors(@PathVariable("id") Integer id, Model model) {
        List<Follower> authors = userService.getAuthors(id);
        model.addAttribute("authors", authors);
        return "user/authorsTemplate";

    }

    @GetMapping("/profile/{id}/follower")
    public String getFollowers(@PathVariable("id") Integer id, Model model) {

        List<Follower> followers = userService.getFollowers(id);
        model.addAttribute("followers", followers);

        return "user/followersTemplate";
    }

    //    follow unfollow для бесплатной подписки, тыкаешь в одно место и кнопка меняется
//    чтобы сделать подписку платной - тыкаешь на другую кнопку
    @PostMapping("/follow/{id}")
    public String followToUser(@PathVariable("id") Integer id,
                               @AuthenticationPrincipal User currentUser) {
        userService.follow(id, currentUser);
        return "redirect:/profile/" + currentUser.getId() + "/author";

    }


    @GetMapping("/hello")
    public String securityUrl() {
        return "user/helloTemplate";
    }
}
