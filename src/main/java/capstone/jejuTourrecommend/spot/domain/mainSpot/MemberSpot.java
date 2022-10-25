package capstone.jejuTourrecommend.spot.domain.mainSpot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import capstone.jejuTourrecommend.authentication.domain.Member;
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
@ToString(of = {"id", "score", "member", "spot"})
public class MemberSpot {

	@Id
	@GeneratedValue
	@Column(name = "member_spot_id")
	private Long id;

	private Double score;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spot_id")
	private Spot spot;

	public MemberSpot(Double score) {
		this.score = score;
	}

	private void changeMember(Member member) {
		this.member = member;
		member.getMemberSpotList().add(this);
	}

	private void changeSpot(Spot spot) {
		this.spot = spot;
		spot.getMemberSpotList().add(this);
	}

	public MemberSpot(Double score, Member member, Spot spot) {
		this.score = score;
		if (spot != null && member != null) {
			changeMember(member);
			changeSpot(spot);
		}

	}
}

























