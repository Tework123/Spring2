package springApp2.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springApp2.models.Photo;
import springApp2.models.Post;
import springApp2.models.User;
import springApp2.repositories.PostRepository;
import springApp2.repositories.UserRepository;
import springApp2.utils.FileUploadUtil;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Post> listPosts() {
        return postRepository.findAll();
    }

    public Post createPost(Principal principal, Post post, MultipartFile file1, MultipartFile file2) throws IOException {
        post.setUser(getUserByPrincipal(principal));

        Photo photo1 = toImageEntity(file1);
        post.addPhotoToPost(photo1);
        Photo photo2 = toImageEntity(file2);
        post.addPhotoToPost(photo2);
        return postRepository.save(post);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return new User();
        }
        return userRepository.findByEmail(principal.getName());

    }

    private Photo toImageEntity(MultipartFile file1) throws IOException {
        Photo photo = new Photo();
        String fileName1 = StringUtils.cleanPath(file1.getOriginalFilename());

        photo.setName(fileName1);
        return photo;

    }

    public void savePhotos(MultipartFile file1, MultipartFile file2,
                           Post savedPost) throws IOException {
        String fileName = StringUtils.cleanPath(file1.getOriginalFilename());
        String fileName2 = StringUtils.cleanPath(file2.getOriginalFilename());

        String uploadDir = "src/main/resources/static/photos/" + savedPost.getId();

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


}






