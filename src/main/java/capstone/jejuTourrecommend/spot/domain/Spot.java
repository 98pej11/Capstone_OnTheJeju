package capstone.jejuTourrecommend.spot.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Review;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;
import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;
import capstone.jejuTourrecommend.wishList.domain.FavoriteSpot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SequenceGenerator(
	name = "SPOT_SEQ_GENERATOR",
	sequenceName = "SPOT_SEQ",
	initialValue = 1,
	allocationSize = 50
)
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString(of={"id","username","address","description","location"}) //원래 연관관계 없는 필드만 넣어야 서로 toSting 하면 무한루프 발생함
//@Table(indexes = @Index(name = "i_spot", columnList = "spot_id"))
public class Spot {

	@Id
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "SPOT_SEQ_GENERATOR"
	)
	@Column(name = "spot_id")
	private Long id;

	private String name;
	private String address;

	@Lob
	private String description;

	@Enumerated(EnumType.STRING)
	private Location location;  //제주 읍 위치

	//@Enumerated(EnumType.STRING)
	//private Category category;

	@OneToMany(mappedBy = "spot")
	private List<FavoriteSpot> favoriteSpotList = new ArrayList<>();

	@OneToMany(mappedBy = "spot")
	private List<MemberSpot> memberSpotList = new ArrayList<>();

	@OneToMany(mappedBy = "spot")
	private List<Review> reviews = new ArrayList<>();

	@OneToMany(mappedBy = "spot")
	private List<Picture> pictures = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "score_id")
	private Score score;

	private void changeScore(Score score) {
		this.score = score;
		score.setSpot(this);
	}

	public Spot(String name) {
		this.name = name;
	}

	public Spot(String name, Score score) {
		this.name = name;
		if (score != null) {
			changeScore(score);
		}
	}

	public Spot(Location location, Score score) {
		this.location = location;
		if (score != null) {
			changeScore(score);
		}
	}

}




























