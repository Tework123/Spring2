package springApp2.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import springApp2.models.*;
import springApp2.models.enums.StatusPost;
import springApp2.repositories.*;
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
    private final UserPostRepository userPostRepository;


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

    public void setPostStatus(Integer id, User currentUser) {
//        получаем пост по id
//        проверяем, если он уже в таблице manytomany, то чекаем set, если там лайк
//        то ставим дизлайк. В посте убираем лайк, добавляем дизлайк
//        если в таблице нет такой записи, то создаем, ставим лайк, кнопка меняется на дизлайк
//        В посте добавляем дизлайк
        Post post = postRepository.findById(id).orElse(null);
        UserPost oldUserPost = userPostRepository.findByPostIdAndUser(id, currentUser);
        if (oldUserPost == null) {

            UserPostKey key = new UserPostKey();
            key.setPostid(post.getId());
            key.setUserid(currentUser.getId());

            UserPost newUserPost = new UserPost();
            newUserPost.setId(key);
            newUserPost.setPost(post);
            newUserPost.setUser(currentUser);
            newUserPost.getStatus().add(StatusPost.LIKE);
            userPostRepository.save(newUserPost);

            post.getPostStatus().add(newUserPost);
            currentUser.getPostStatus().add(newUserPost);
            post.setLikes(post.getLikes() + 1);
            postRepository.save(post);
            userRepository.save(currentUser);

        } else {
            if (oldUserPost.getStatus().contains(StatusPost.LIKE)) {

                oldUserPost.getStatus().clear();
                oldUserPost.getStatus().add(StatusPost.DISLIKE);
                userPostRepository.save(oldUserPost);

                post.setLikes(post.getLikes() - 1);
                post.setDislikes(post.getDislikes() + 1);
                post.getPostStatus().clear();
                post.getPostStatus().add(oldUserPost);
                postRepository.save(post);

            } else {
                oldUserPost.getStatus().clear();
                oldUserPost.getStatus().add(StatusPost.LIKE);
                userPostRepository.save(oldUserPost);

                post.setLikes(post.getLikes() + 1);
                post.setDislikes(post.getDislikes() - 1);

//                вот тут не надо чистить getPostStatus, он же для поста
//                а юзеров может быть много, сверху тоже поправить этот момент
//                и с юзерами посмотреть
//                и почекать закреп stackoverflow
                post.getPostStatus().clear();
                post.getPostStatus().add(oldUserPost);
                postRepository.save(post);

            }

        }

        System.out.println(post.getLikes());
        System.out.println(post.getDislikes());

//        System.out.println(post.getPostStatus());
        for (UserPost o : post.getPostStatus()) {
            System.out.println(o);
        }

        System.out.println("_________________________________");


    }


}






