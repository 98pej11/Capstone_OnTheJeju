package capstone.jejuTourrecommendV2.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","url","spot"})
public class Picture {


    @Id @GeneratedValue
    @Column(name = "picture_id")
    private Long id;

    @Lob
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    public Picture(String url,Spot spot) {
        this.url = url;
        if(spot!=null){
            changeSpot(spot);
        }
    }

    private void changeSpot(Spot spot) {
        this.spot = spot;
        spot.getPictures().add(this);
    }


}






























