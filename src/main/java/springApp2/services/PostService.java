package springApp2.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springApp2.models.Post;
import springApp2.repositories.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> listPosts() {
        return postRepository.findAll();
    }

    public void createPost(Post post) {
        postRepository.save(post);
    }

    public Post getPost(Integer id) {
        return postRepository.findById(id).orElse(null);

    }

    public void editPost(Integer id, Post editPost) {
        Post oldPost = postRepository.findById(id).orElse(null);
        oldPost.setName(editPost.getName());
        oldPost.setText(editPost.getText());
        postRepository.save(oldPost);
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }


}






