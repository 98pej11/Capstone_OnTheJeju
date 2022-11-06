package capstone.jejuTourrecommend.spot.domain.detailSpot;

import capstone.jejuTourrecommend.spot.domain.Spot;
import lombok.*;

import javax.persistence.*;

@SequenceGenerator(
	name = "REVIEW_SEQ_GENERATOR",
	sequenceName = "REVIEW_SEQ",
	initialValue = 1,
	allocationSize = 50
)
@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "content"})
public class Review {

	@Id
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "REVIEW_SEQ_GENERATOR"
	)
	@Column(name = "review_id")
	private Long id;

	@Lob
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spot_id")
	private Spot spot;

	public Review(String content, Spot spot) {
		this.content = content;
		if (spot != null) {
			changeSpot(spot);
		}
	}

	private void changeSpot(Spot spot) {
		this.spot = spot;
		spot.getReviews().add(this);
	}
}


























