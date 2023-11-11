package springApp2.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springApp2.models.Post;
import springApp2.models.User;

import java.util.List;
import java.util.Objects;

public interface PostRepository extends JpaRepository<Post, Integer> {
    public List<Post> findAllByOrderByDateCreateDesc();

    public List<Post> findPostByUserInOrderByDateCreateDesc(@Param("authors") List<User> authors);

    public List<Post> findByUser(User currentUser);

}
