package springApp2.models;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class UserPostKey implements Serializable {

    @Column(name = "user_id")
    private Integer userid;

    @Column(name = "post_id")
    private Integer postid;
}
