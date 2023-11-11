package springApp2.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "photos")
@Data
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;

    //  в реальности этого поля в таблице нет
    @OneToOne(mappedBy = "avatar")
    @EqualsAndHashCode.Exclude
    private User user;

    @Transient
    public String getPhotoImagePath() {
        if (name == null || id == null) {
            return null;
        }
        if (post.getUser() == null) {
            return null;
        }
        return post.getUser().getEmail() + "/" + post.getId() + "/" + name;
    }

    @Transient
    public String getAvatarPath() {
        if (name == null || id == null) {
            return null;
        }
        return user.getEmail() + "/" + name;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
