package springApp2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springApp2.models.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    public List<Post> findAllByOrderByDateCreateDesc();


}
