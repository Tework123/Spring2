package springApp2.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import springApp2.models.Follower;
import springApp2.models.User;
import springApp2.repositories.UserRepository;
import springApp2.services.PostService;
import springApp2.services.UserService;

import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
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
        User author = null;
        if (currentUser != null) {
            List<Follower> authors = userService.getAuthors(currentUser.getId());
            for (int i = 0; i < authors.size(); i++) {
                if (authors.get(i).getUserAuthor().getId() == user.getId()) {
                    author = user;
                    break;
                }
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

    @DeleteMapping("/profile/delete")
    public String deleteProfile(
            @AuthenticationPrincipal User currentUser,
            HttpServletRequest request,
            HttpServletResponse response) {
//      убираем куки
        Cookie cookie = new Cookie("remember-me", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        postService.getMyPosts(currentUser);

//      убираем юзера из текущей сессии
        if (request.getSession() != null) {
            request.getSession().invalidate();
        }

//      удаляем юзера из базы данных
        userRepository.deleteById(currentUser.getId());
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

    @PostMapping("/follow/{id}")
    public String followToUser(@PathVariable("id") Integer id,
                               @AuthenticationPrincipal User currentUser) {
        userService.follow(id, currentUser);
        return "redirect:/profile/" + currentUser.getId() + "/author";

    }

    // LIKED POSTS
    @GetMapping("/profile/likedPosts")
    public String getLikedPosts(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("likedPosts", postService.getLikedPosts(currentUser));
        return "user/likedPostsTemplate";
    }


    @GetMapping("/hello")
    public String securityUrl() {
        return "user/helloTemplate";
    }
}
