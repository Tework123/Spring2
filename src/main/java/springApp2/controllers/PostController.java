package springApp2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springApp2.models.Post;
import springApp2.services.PostService;
import jakarta.validation.Valid;

import java.io.IOException;
import java.security.Principal;


@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    //  RequiredArgsConstructor заменяет конструктор
    private final PostService postService;

    @GetMapping("")
    public String getPosts(Model model, Principal principal) {
        model.addAttribute("posts", postService.listPosts());
        model.addAttribute("user", postService.getUserByPrincipal(principal));
        return "post/getPostsTemplate";

    }

    @GetMapping("/createPost")
    public String createPostHtml(Post post) {
        return "post/createPostTemplate";
    }

    @PostMapping("")
    public String createPost(@Valid Post post, @RequestParam("file1") MultipartFile file1,
                             MultipartFile file2, Principal principal,

                             BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "post/createPostTemplate";
        }

        Post savedPost = postService.createPost(principal, post, file1, file2);
        postService.savePhotos(file1, file2, savedPost);

        return "redirect:/post";

    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Integer id, Model model) {
        Post post = postService.getPost(id);
        System.out.println(post.getPhotos());

        model.addAttribute("post", post);
        for (int i = 0; i < post.getPhotos().size(); i++) {
            post.getPhotos().get(i).getPhotoImagePath();
        }
        model.addAttribute("photos", post.getPhotos());


        return "post/getPostTemplate";
    }

    @GetMapping("/{id}/editPost")
    public String editPostHtml(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "post/editPostTemplate";
    }

    //  порядок аргументов в методе важен!
    @PatchMapping("/{id}")
    public String editPost(@Valid Post post, BindingResult bindingResult, @PathVariable("id") Integer id
    ) {
        if (bindingResult.hasErrors()) {
            return "post/editPostTemplate";
        }
        postService.editPost(id, post);
        return "redirect:/post";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") Integer id) {
        postService.deletePost(id);
        return "redirect:/post";
    }
}







