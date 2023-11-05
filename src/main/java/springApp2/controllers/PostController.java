package springApp2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springApp2.models.Post;
import springApp2.services.PostService;
import jakarta.validation.Valid;

import java.io.IOException;


@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public String getPosts(Model model) {
        model.addAttribute("posts", postService.listPosts());
        return "post/getPosts";

    }

    @GetMapping("/createPost")
    public String createPostHtml(Post post) {
        return "post/createPost";
    }

    @PostMapping("")
    public String createPost(@Valid Post post, @RequestParam("file1") MultipartFile file1,
                             MultipartFile file2,

                             BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "post/createPost";
        }

        Post savedPost = postService.createPost(post, file1, file2);
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


        return "post/getPost";
    }

    @GetMapping("/{id}/editPost")
    public String editPostHtml(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "post/editPost";
    }

    //  порядок аргументов в методе важен!
    @PatchMapping("/{id}")
    public String editPost(@Valid Post post, BindingResult bindingResult, @PathVariable("id") Integer id
    ) {
        if (bindingResult.hasErrors()) {
            return "post/editPost";
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







