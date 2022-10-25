package capstone.jejuTourrecommend.spot.domain.detailSpot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import capstone.jejuTourrecommend.spot.domain.Spot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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


























