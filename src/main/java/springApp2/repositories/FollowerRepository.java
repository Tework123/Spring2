package springApp2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springApp2.models.Follower;
import springApp2.models.User;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {

    //  ищем всех авторов, на которых подписан этот follower
    List<Follower> findByUserFollowerId(Integer id);

    List<Follower> findByUserAuthorId(Integer id);

    Follower findByUserFollowerIdAndUserAuthorId(Integer followerId, Integer authorId);


}
