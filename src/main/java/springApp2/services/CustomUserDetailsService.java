package springApp2.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springApp2.repositories.UserRepository;
import springApp2.models.User;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not authorized.");
        }
//        GrantedAuthority authority = new SimpleGrantedAuthority(activeUserInfo.getRole());
//        UserDetails userDetails = (UserDetails)new User(dBuserName,
//                activeUserInfo.getPassword(), Arrays.asList(authority));
//        return userDetails;

        return userRepository.findByEmail(email);
    }
}
