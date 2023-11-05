package springApp2.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springApp2.models.Photo;
import springApp2.models.Post;
import springApp2.repositories.PostRepository;
import springApp2.utils.FileUploadUtil;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> listPosts() {
        return postRepository.findAll();
    }

    public void createPost(Post post, MultipartFile file1, MultipartFile file2) throws IOException {
        Photo photo1;
        Photo photo2;
        if (file1.getSize() != 0) {
            photo1 = toImageEntity(file1);
            photo1.setPreviewImage(true);
            post.addPhotoToPost(photo1);
        }
        if (file2.getSize() != 0) {
            photo2 = toImageEntity(file2);
            post.addPhotoToPost(photo2);
        }
//      устанавливаем первое фото для поста
        Post postFromDb = postRepository.save(post);
        postFromDb.setPreviewPhotoId(postFromDb.getPhotos().get(0).getId());
        postRepository.save(post);
    }

    private Photo toImageEntity(MultipartFile file1) throws IOException {
        Photo photo = new Photo();
        photo.setName(file1.getName());
        photo.setOriginalFileName(file1.getOriginalFilename());
        photo.setContentType(file1.getContentType());
        photo.setSize(file1.getSize());
        photo.setBytes(file1.getBytes());
        return photo;

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






