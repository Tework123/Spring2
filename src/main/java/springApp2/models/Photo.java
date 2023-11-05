package springApp2.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;

@Entity
@Table(name = "photos")
@Data
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Post post;

    @Transient
    public String getPhotoImagePath() {
        if (name == null || id == null) {
            return null;
        }
        return post.getId() + "/" + name;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", post=" + post +
                '}';
    }
}
