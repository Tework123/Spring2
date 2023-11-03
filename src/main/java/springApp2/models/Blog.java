package springApp2.models;

import java.io.Serializable;
//
//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//
//@Entity
//@Table(name = "blog", uniqueConstraints = {
//        @UniqueConstraint(columnNames = "id"),
//        @UniqueConstraint(columnNames = "email")})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {
    private String title;
    private int age;

//
//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    @Column(name = "id", unique = true, nullable = false)
//    private Integer id;
//
//
//    @Column(name = "email", unique = true, nullable = false, length = 100)
//    private String email;
//
//    @Column(name = "firstname", unique = false, nullable = false, length = 100)
//    private String firstname;
//
//    @Column(name = "lastname", unique = false, nullable = false, length = 100)
//    private String lastname;
//
//    @Override
//    public String toString() {
//        return "Blog{" +
//                "id=" + id +
//                ", email='" + email + '\'' +
//                ", firstname='" + firstname + '\'' +
//                ", lastname='" + lastname + '\'' +
//                '}';
//    }
}