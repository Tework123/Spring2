package springApp2.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springApp2.models.enums.Role;

import java.time.LocalDate;
import java.util.*;


@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column()
    @Size(min = 3, max = 30, message = "name length should 3 to 30")
    private String name;

    @Column(unique = true)
    @Size(min = 3, max = 50, message = "email length should 3 to 50")
    private String email;

    @Column(length = 1000)
    private String password;

    private boolean active;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "photo_id")
    private Photo avatar;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    private LocalDate dateJoined;

    private void init() {
        dateJoined = LocalDate.now();
    }

    //  OneToMany = ElementCollection без @Entity
    //  Создаем доп. таблицу user_role, она связана с user по полю user_id, в ней только String
    //  Значения для этой таблицы берутся из Enumerated
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}






