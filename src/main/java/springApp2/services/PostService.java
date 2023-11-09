package springApp2.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springApp2.models.Follower;
import springApp2.models.Photo;
import springApp2.models.Post;
import springApp2.models.User;
import springApp2.repositories.FollowerRepository;
import springApp2.repositories.FollowerUserAuthor;
import springApp2.repositories.PostRepository;
import springApp2.repositories.UserRepository;
import springApp2.utils.FileUploadUtil;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PhotoService photoService;
    private final FollowerRepository followerRepository;


    public List<Post> listPosts() {
        return postRepository.findAllByOrderByDateCreateDesc();
    }


    public Post createPost(User currentUser, Post post, MultipartFile file1, MultipartFile file2) throws IOException {
        post.setUser(currentUser);
        Photo photo1 = photoService.toImageEntity(file1, currentUser);
        Photo photo2 = photoService.toImageEntity(file2, currentUser);
        post.addPhotoToPost(photo1);
        post.addPhotoToPost(photo2);
        return postRepository.save(post);

    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new User();
        }
        return userRepository.findByEmail(principal.getName());

    }

    public void savePhotos(MultipartFile file1, MultipartFile file2, Post savedPost, @AuthenticationPrincipal User currentUser) throws IOException {
        String fileName = StringUtils.cleanPath(file1.getOriginalFilename());
        String fileName2 = StringUtils.cleanPath(file2.getOriginalFilename());
        String uploadDir = "src/main/resources/static/photos/" + currentUser.getEmail() + "/" + savedPost.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, file1);
        FileUploadUtil.saveFile(uploadDir, fileName2, file2);

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

    public List<Post> getFollowPost(Integer id) {
        List<Follower> followAuthors = followerRepository.findByUserFollowerId(id);
        List<User> authors = new ArrayList<>();

        for (int i = 0; i < followAuthors.size(); i++) {
            authors.add(followAuthors.get(i).getUserAuthor());
        }

        return postRepository.findPostByUserInOrderByDateCreateDesc(authors);

    }


}






