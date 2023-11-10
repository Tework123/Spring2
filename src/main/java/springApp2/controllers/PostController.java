package springApp2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import springApp2.models.Post;
import springApp2.models.User;
import springApp2.services.PostService;
import jakarta.validation.Valid;

import java.io.IOException;


@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    //  RequiredArgsConstructor заменяет конструктор
    private final PostService postService;

    @GetMapping("")
    public String getPosts(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("posts", postService.listPosts());
        model.addAttribute("currentUser", currentUser);

        return "post/getPostsTemplate";

    }
//    сделать many to many с лайками по статье,
//    через sets, можно и с композитным ключом попробовать
//    далее тесты посмотреть, линтеры, докер и остальное для хоста, если инета не будет,то
//    начинам rest проект

    @GetMapping("/createPost")
    public String createPostHtml(Post post) {
        return "post/createPostTemplate";
    }

    @PostMapping("")
    public String createPost(@Valid Post post, @RequestParam("file1") MultipartFile file1,
                             MultipartFile file2, @AuthenticationPrincipal User currentUser,
                             BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "post/createPostTemplate";
        }

        Post savedPost = postService.createPost(currentUser, post, file1, file2);
        postService.savePhotos(file1, file2, savedPost, currentUser);
        return "redirect:/post";

    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Integer id,
                          @AuthenticationPrincipal User currentUser,
                          Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        model.addAttribute("photos", post.getPhotos());
        model.addAttribute("currentUser", currentUser);

        return "post/getPostTemplate";
    }

    @GetMapping("/{id}/editPost")
    public String editPostHtml(@PathVariable("id") Integer id,
                               @AuthenticationPrincipal User currentUser,
                               Model model) {

        Post post = postService.getPost(id);
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Не твой пост");

        }
        if (currentUser != null) {
            if (currentUser.getId() != post.getUser().getId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Не твой пост");
            }
        }

        model.addAttribute("post", post);
        return "post/editPostTemplate";
    }

    //  порядок аргументов в методе важен!
    @PatchMapping("/{id}")
    public String editPost(@Valid Post post,
                           BindingResult bindingResult,
                           @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "post/editPostTemplate";
        }
//      сюда все еще можно отправить patch запрос на изменение данных даже без регистрации
//      но это не rest, поэтому через html не получится?
        postService.editPost(id, post);
        return "redirect:/post";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") Integer id) {
        postService.deletePost(id);
        return "redirect:/post";
    }


    @GetMapping("/followPosts")
    public String getFollowPost(@AuthenticationPrincipal User currentUser,
                                Model model
    ) {

        model.addAttribute("posts", postService.getFollowPost(currentUser.getId()));

        return "post/followPostsTemplate";

    }

    //    POST STATUS (LIKES, DISLIKES)
    @PostMapping("/{id}/status")
    public String setPostStatus(@PathVariable("id") Integer id,
                                @AuthenticationPrincipal User currentUser) {
        postService.setPostStatus(id, currentUser);
        return "redirect:/post/" + id;

    }
}







