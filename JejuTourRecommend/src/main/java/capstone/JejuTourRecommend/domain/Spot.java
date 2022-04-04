package capstone.JejuTourRecommend.domain;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","username","address","description"})
public class Spot {


    @Id @GeneratedValue
    @Column(name = "spot_id")
    private Long id;
    private String name;
    private String address;
    private String description;

    @Enumerated(EnumType.STRING)
    private Location location;  //제주 읍 위치


    @OneToMany(mappedBy = "spot")
    private List<FavoriteSpot> favoriteSpotList = new ArrayList<>();

    @OneToMany(mappedBy = "spot")
    private List<MemberSpot> memberSpotList = new ArrayList<>();

    @OneToMany(mappedBy = "spot")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "spot")
    private List<Picture> pictures = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "score_id")
    private Score score;

    public Spot(String name) {
        this.name = name;
    }

    public Spot(String name,Score score) {
        this.name = name;
        this.score = score;
    }
}






























