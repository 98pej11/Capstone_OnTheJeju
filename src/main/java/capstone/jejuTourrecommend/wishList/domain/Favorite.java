package capstone.jejuTourrecommend.wishList.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import capstone.jejuTourrecommend.authentication.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SequenceGenerator(
	name = "FAVORITE_SEQ_GENERATOR",
	sequenceName = "FAVORITE_SEQ",
	initialValue = 1,
	allocationSize = 50
)
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Favorite {

	@Id
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "FAVORITE_SEQ_GENERATOR"
	)
	@Column(name = "favorite_id")
	private Long id;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "favorite")
	private List<FavoriteSpot> favoriteSpotList = new ArrayList<>();

	public Favorite(String name) {
		this.name = name;
	}

	public Favorite(String name, Member member) {
		this.name = name;
		if (member != null) {
			changeMember(member);
		}
	}

	private void changeMember(Member member) {
		this.member = member;
		member.getFavorites().add(this);
	}
}










