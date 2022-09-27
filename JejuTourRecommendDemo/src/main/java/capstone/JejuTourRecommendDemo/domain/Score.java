package capstone.JejuTourRecommendDemo.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","rankAverage"})
public class Score {

    @Id @GeneratedValue
    @Column(name = "score_id")
    private Long id;

    private Double viewScore;
    private Double priceScore;
    private Double facilityScore;
    private Double surroundScore;

    private Double viewRank;
    private Double priceRank;
    private Double facilityRank;
    private Double surroundRank;

    private Double rankAverage;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "score")
    private Spot spot;



}
















