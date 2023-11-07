package springApp2.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import springApp2.models.Photo;
import springApp2.models.Post;
import springApp2.models.User;
import springApp2.models.enums.Role;
import springApp2.repositories.UserRepository;
import springApp2.utils.FileUploadUtil;

import java.io.IOException;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhotoService photoService;

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

    public void savePhotos(MultipartFile file,
                           User savedUser) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "src/main/resources/static/photos/"
                + savedUser.getEmail();
        FileUploadUtil.saveFile(uploadDir, fileName, file);
    }
}









