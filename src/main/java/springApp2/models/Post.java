package springApp2.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "post")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name", unique = false, nullable = false, length = 50)
    private String name;

    @Column(name = "text", unique = false, nullable = false, columnDefinition = "text")
    private String text;

    @CreationTimestamp
    @Column(name = "datePublish", unique = false, nullable = false)
    private Date datePublish;
}







