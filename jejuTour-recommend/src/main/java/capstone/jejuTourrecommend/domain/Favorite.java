package capstone.jejuTourrecommend.domain;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","name"})
public class Favorite {


    @Id @GeneratedValue
    @Column(name = "favorite_id")
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "favorite")
    private List<FavoriteSpot> favoriteSpotList = new ArrayList<>();


    public Favorite(String name) {
        this.name = name;
    }

    public Favorite(String name, User user) {
        this.name = name;
        if(user!=null){
            changeMember(user);
        }
    }

    private void changeMember(User user) {
        this.user = user;
        user.getFavorites().add(this);
    }
}












