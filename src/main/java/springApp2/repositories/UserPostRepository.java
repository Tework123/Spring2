package springApp2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springApp2.models.User;
import springApp2.models.UserPost;
import springApp2.models.enums.StatusPost;

//import static springApp2.models.enums.StatusPost.LIKE;
import java.util.List;
import java.util.Set;


public interface UserPostRepository extends JpaRepository<UserPost, Integer> {
    UserPost findByPostIdAndUser(Integer id, User currentuser);
    List<UserPost> findByUserAndStatus(User currentuser, StatusPost statusPost);


}
