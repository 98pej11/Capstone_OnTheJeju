package capstone.jejuTourrecommend.spot.domain.detailSpot;

import capstone.jejuTourrecommend.spot.domain.Spot;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "url", "spot"})
@Table(name = "Picture", indexes = @Index(name = "spot_id_picture_id", columnList = "spot_id, picture_id"))
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






























