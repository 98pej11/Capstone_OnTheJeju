package capstone.jejuTourrecommend.spot.domain.detailSpot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import capstone.jejuTourrecommend.spot.domain.Spot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "url", "spot"})
public class Picture {

	@Id
	@GeneratedValue
	@Column(name = "picture_id")
	private Long id;

	@Lob
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spot_id")
	private Spot spot;

	public Picture(String url, Spot spot) {
		this.url = url;
		if (spot != null) {
			changeSpot(spot);
		}
	}

	private void changeSpot(Spot spot) {
		this.spot = spot;
		spot.getPictures().add(this);
	}

}






























