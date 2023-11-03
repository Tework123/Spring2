package springApp2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springApp2.models.Blog;
import springApp2.services.BlogService;


@Controller
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("blogs", blogService.listBlogs());
        System.out.println(blogService.listBlogs());
        System.out.println("asd");
        return "blog/index";

    }

    @GetMapping("/create")
    public String createBlog0(@ModelAttribute("blog") Blog blog) {
        return "blog/new";
    }

    @PostMapping("/create")
    public String createBlog(Blog blog) {
        blogService.saveBlog(blog);
        return "redirect:/";

    }
}
