package springApp2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springApp2.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    //  по названию метода понимает, что нужно делать
    User findByEmail(String email);

    Optional<User> findById(Integer id);
}
