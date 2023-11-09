package springApp2.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import springApp2.models.Follower;
import springApp2.models.Photo;
import springApp2.models.Post;
import springApp2.models.User;
import springApp2.models.enums.Role;
import springApp2.models.enums.StatusFollow;
import springApp2.repositories.FollowerRepository;
import springApp2.repositories.UserRepository;
import springApp2.utils.FileUploadUtil;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhotoService photoService;
    private final FollowerRepository followerRepository;

    public boolean createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
//          перенаправляет на error.html
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exist");
        }

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        userRepository.save(user);
        return true;
    }

    public User editProfile(User editUser, User oldUser, MultipartFile file) throws IOException {
        oldUser.setName(editUser.getName());
        Photo photo = photoService.toImageEntity(file, oldUser);
        oldUser.setAvatar(photo);
        return userRepository.save(oldUser);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void userBan(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
            } else {
                user.setActive(true);
            }
            userRepository.save(user);
        }

    }

    public void setRole(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.getRoles().contains(Role.ROLE_USER)) {
                user.getRoles().clear();
                user.getRoles().add(Role.ROLE_ADMIN);
            } else {
                user.getRoles().clear();
                user.getRoles().add(Role.ROLE_USER);
            }
            userRepository.save(user);
        }

    }

    public void follow(Integer id, User currentUser) {
        User user = userRepository.findById(id).orElse(null);
        Follower oldFollow = followerRepository.findByUserFollowerIdAndUserAuthorId(currentUser.getId(), id);
        if (oldFollow != null) {
            followerRepository.deleteById(oldFollow.getId());
        } else {
            Follower follow = new Follower();

            //      установка подписчика
            follow.setUserFollower(currentUser);
            follow.getStatusFollow().add(StatusFollow.FREE_FOLLOW);
            //      установка автора
            follow.setUserAuthor(user);
            followerRepository.save(follow);

        }
    }

    public List<Follower> getAuthors(Integer id) {
        return followerRepository.findByUserFollowerId(id);
    }

    public List<Follower> getFollowers(Integer id) {
        return followerRepository.findByUserAuthorId(id);
    }


    public void savePhotos(MultipartFile file, User savedUser) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "src/main/resources/static/photos/" + savedUser.getEmail();
        FileUploadUtil.saveFile(uploadDir, fileName, file);
    }
}









