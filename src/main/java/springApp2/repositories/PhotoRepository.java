package springApp2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springApp2.models.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

}
