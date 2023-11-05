package springApp2.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 3, max = 30, message = "3 to 30")
    private String name;

    @Column(name = "text", unique = false, nullable = false, columnDefinition = "text")
    private String text;

    @Column(nullable = true, length = 100)
    private String photo;

//    public void addPhotoToPost(Photo photo) {
//        photo.setPost(this);
//        photos.add(photo);
//    }

    @Transient
    public String getPhotoImagePath() {
        if (photo == null || id == null) return null;

        return id + "/" + photo;
    }

    private LocalDateTime dateCreate;

    @PrePersist
    private void init() {
        dateCreate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", dateCreate=" + dateCreate +
                '}';
    }

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
//    private List<Photo> photos = new ArrayList<>();
//
//    private Integer previewPhotoId;

}







