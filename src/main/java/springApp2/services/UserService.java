package springApp2.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springApp2.models.Post;
import springApp2.models.User;
import springApp2.models.enums.Role;
import springApp2.repositories.UserRepository;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public void editProfile(User editUser, User oldUser) {
        oldUser.setName(editUser.getName());

        userRepository.save(oldUser);
    }
}









