package springApp2.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import springApp2.models.enums.Role;
import springApp2.models.enums.StatusPost;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class UserPost {

    @EmbeddedId
    private UserPostKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    @EqualsAndHashCode.Exclude
    private Post post;

    @ElementCollection(targetClass = StatusPost.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_post_status", joinColumns
            = {@JoinColumn(name = "post_id"), @JoinColumn(name = "user_id")})
    @Enumerated(EnumType.STRING)
    private Set<StatusPost> status = new HashSet<>();
}








