package capstone.jejuTourrecommend.domain;


import lombok.*;
import lombok.extern.java.Log;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","username","email","password"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private String email;
    private String password;


    @OneToMany(mappedBy = "member")
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberSpot> memberSpotList = new ArrayList<>();

    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Member(String email) {
        this.email = email;
    }
}


















