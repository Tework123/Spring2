package springApp2.models;


import jakarta.persistence.*;
import lombok.Data;
import springApp2.models.enums.Role;
import springApp2.models.enums.StatusFollow;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "followers")
@Data
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_follower")
    User userFollower;

    @ManyToOne
    @JoinColumn(name = "user_author")
    User userAuthor;

    private LocalDate dateFollow;
    @PrePersist
    private void init() {
        dateFollow = LocalDate.now();
    }


    @ElementCollection(targetClass = StatusFollow.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "follow_status_follow", joinColumns = @JoinColumn(name = "follower_id"))
    @Enumerated(EnumType.STRING)
    private Set<StatusFollow> statusFollow = new HashSet<>();

//    надо сделать метод, который ищет по id пользователя follow сущность, как я делал
//    с друзьями в социальной сети id == user_1 or id ==user_2

}



