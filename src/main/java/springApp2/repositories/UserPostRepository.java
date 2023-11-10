package springApp2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springApp2.models.Follower;
import springApp2.models.User;
import springApp2.models.UserPost;

import java.util.List;

public interface UserPostRepository extends JpaRepository<UserPost, Integer> {
    UserPost findByPostIdAndUser(Integer id, User currentuser);

}
