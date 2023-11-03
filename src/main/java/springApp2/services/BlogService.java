package springApp2.services;


import org.springframework.stereotype.Service;
import springApp2.models.Blog;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {
    private List<Blog> blogs = new ArrayList<>();

    {
        blogs.add(new Blog("ads", 1));
        blogs.add(new Blog("ad2s", 2));
        blogs.add(new Blog("ads3", 3));
    }

    public List<Blog> listBlogs() {
        return blogs;
    }

    public void saveBlog(Blog blog) {
        blogs.add(blog);
    }

    public void deleteBlog(int age) {
        blogs.removeIf(blog -> blog.getAge() == age);
    }
}
