## 2. 객체지항의 오해와 사실, 디자인 패턴 적용

- “객체 지향의 사실과 오해” 책을 통해 객체 지향의 의미를 좀 더 이해할 수 있는 계기 되었습니다. 그래서 "객체 지향 언어인 자바"를 책에서 말한 역할, 책임, 협력의 관점으로 바라보며 설계할 수 있다는 것을
  알게 되었습니다. 이후 "객체 지향의 역할, 책임, 협력"을 23가지 패턴으로 만든 “GOF의 23가지 디자인 패턴”도 학습하여 본 프로젝트에 적용하여 좀더 객체 지향적인 코드로 바꾸었습니다

([디자인 패턴 학습 내용 링크](https://github.com/suheonjoo/Study-Document/tree/master/%EB%94%94%EC%9E%90%EC%9D%B8%20%ED%8C%A8%ED%84%B4%20%EC%A0%95%EB%A6%AC))

### (1) "관광지 위치" 전략 패턴 적용 -> 더 효과적인 enum 활용을 알게 되어 업데이트

- 전략 패턴: “상황내용을 포함하는(가지고 있는) 역할”과 “상황에 따른 다양한 전략을 포함하는 역할”을 나누어 전략들을 분리하는 패턴을 만들었습니다 저는 동서남북의 클래스를 따로 분리하여 "위치 정보를 가지고
  있는 역할"을 만들고, 이러한 "위치 정보를 관리하는 역할" LocationStrategy 인터페이스를 만들어 객체들간의 협력 관계를 만들었습니다

<img  alt="stragetyPatternPackage" src="./img/stragetyPatternPackage.png?raw=true"  >
<img  alt="stragetyPatternExample" src="./img/stragetyPatternExample.png?raw=true"  >

- 전략 패턴을 사용한 이유: 현재 동서남북으로 위치정보를 분리하 것은 설문조사와 각 읍별 관광지의 개수를 고려하여 저희 임의의 적절한 지억을 나누었습니다. 이는 관광지가 새로 생길수 있어 지역별 관광지 개수 변경이
  되는 우려가 있었습니다 그래새 유지보수를 더 편리하게 하기 위해서 전략 패턴을 적용하였습니다.

[커밋 경로](https://github.com/suheonjoo/Capstone-JejuTourRecommend/commit/d4cb7eb58c30391103cde2f2489c7b8f27440cda)

<details>

  <summary> 전략 패턴 코드 (👈 Click)</summary>

```
public class DefaultLocation implements LocationStrategy{
    @Override
    public List<Location> getLocation() {

        List<Location> DefaultList = Arrays.asList(Location.Jeju_si, Location.Aewol_eup, Location.Hallim_eup,
                Location.Hangyeong_myeon, Location.Jocheon_eup, Location.Gujwa_eup,
                Location.Daejeong_eup, Location.Andeok_myeon, Location.Seogwipo_si,
                Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup, Location.Udo_myeon
        );

        return DefaultList;
    }
}
```

```

public class EastLocation implements LocationStrategy{
    @Override
    public List<Location> getLocation() {

        List<Location> eastList = Arrays.asList(Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup);
        return eastList;
    }
}
```

```

public interface LocationStrategy {

    List<Location> getLocation();

}

```

```

public class NorthLocation implements LocationStrategy {
    @Override
    public List<Location> getLocation() {

        List<Location> northList = Arrays.asList(Location.Aewol_eup, Location.Jeju_si, Location.Jocheon_eup,
                Location.Gujwa_eup, Location.Udo_myeon);
        return northList;
    }
}

```

```
public class SouthLocation implements LocationStrategy{
    @Override
    public List<Location> getLocation() {

        List<Location> southList = Arrays.asList(Location.Seogwipo_si);
        return southList;
    }
}

public class WestLocation implements LocationStrategy{
    @Override
    public List<Location> getLocation() {
        List<Location> southList = Arrays.asList(Location.Seogwipo_si);
        return southList;
    }
}
```

</details>

<details>

  <summary> enum 타입의 재발견! 본문 확인 (👈 Click)</summary>

#### enum 타입의 재발견

결론은: 1대1 대응관계 있는 집합덩어리라고 생각하게 되었다. 이렇게 결론만 놓고 보면 클래스도 여러 집합을 만들어소 관리할수 있다고 하는데 내가 생각하는 enum 장점은 아래와 같다

1. 공통된 필드로 한클래스에서 관리를 할 수 있다

- 이거 어떻게 보면 클래스에서 인터페이스를 활용하면, 되지 않냐라고 생각할수 있는데 더 큰 이유는 다음 2번에 있다

2. 같은 타입(enum)의 순회가 클래스 보다 자유롭다

- 순회가 자유롭다는 의미는 선언된 같은 enum 타입을 순회 로직을 한 클래스에서 관리할 수 있다는 것이다 -> 이렇게 순회가 자유로우면, 타입을 순회하면서 원하는 타입을 찾거가 공통 연산을 처리하기가 훨씬
  쉬워진다
- ex) 위 예제에서 findYield() 메서드와 같이 타입 순회 수행하면서, 각 필드의 총합을 구하는 예이다 -> 만약 클래스로 다루었으면 인터페이스로 클래스를 상속을 받고 클래스들의 저장공간을 따로 만들어
  순회를 해야한다 -> 그러나 한개의 enum 클래스 안에서 메서드 하나만 작성하면 된다
- 그 외에서 순회를 통해 관리하는 활용도는 무긍무진 하다

3. enum 의 1대1 대응관계의 집합의 특징을 이용해서 변경이 별로 없으며, 내용도 많지 않은 테이블로 관리할 수가 있다

요번 우테코 프리 코스 미션2를 통해 이전 졸업프로젝트로 했던 spring boot 프로젝트 도 개선할 수 있는 아이디어를 얻었다, 해당 프로젝트에서 이전에는 위치정보 저장 값을 전략 패턴을 사용하셔 해결하였다.
그러나 요번 enum 에 대해서 학습을 하니 1대일 관계의 집합이며, 변경도 자주 있지 않으며, 내용도 많지 않는 테이블 형태로 구성되어 있어서 적용하게 딱이다라고 생각하였다. 테이블관계는 아래 코드 위에 주석으로
확인할수 있다

아래 코드를 보면 enum 타입을 enum 타입 그룹으로 테이블 같이 포현하였다.

```
/**
 * 북 : 애월읍,제주시,조천읍,구좌읍,우도면
 * 동 : 남원읍, 표선면, 성산읍
 * 서 : 한림읍, 한경면, 대정읍, 안덕면
 * 남 : 서귀포시
 */
public enum LocationGroup {

	NORTH_LOCATION(List.of(Location.Aewol_eup, Location.Jeju_si, Location.Jocheon_eup, Location.Gujwa_eup, Location.Udo_myeon),"북부"),
	EAST_LOCATION(List.of(Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup),"동부"),
	WEST_LOCATION(List.of(Location.Seogwipo_si),"서부"),
	SOUTH_LOCATION(List.of(Location.Seogwipo_si),"남부"),
	ALL_LOCATION(List.of(Location.Jeju_si, Location.Aewol_eup, Location.Hallim_eup,
		Location.Hangyeong_myeon, Location.Jocheon_eup, Location.Gujwa_eup,
		Location.Daejeong_eup, Location.Andeok_myeon, Location.Seogwipo_si,
		Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup, Location.Udo_myeon), "전체");

	
	private final List<Location> locations;
	private final String KrName;

	LocationGroup(List<Location> locations, String krName) {
		this.locations = locations;
		this.KrName = krName;
	}

	public static List<Location> getLocations(String krName) {
		return Arrays.stream(LocationGroup.values())
			.filter(i -> i.KrName.equals(krName))
			.map(i -> i.locations).findFirst().orElseThrow(() -> new UserException("카테고리의 제대로된 입력값을 넣어야 합니다"));
	}

}
```

먼저 Location 을 enum 타입으로 만들었으며, 이러한 enum 타입을 enum group으로 묶었다 순회하는 메서드는 getLocations()을 통해서 구현하였다.

아래 코드는 최종 서비스 영역에서 개선되 코드이다

```
public List<Location> findLocation(MainPageForm mainPageRequest) {
		if (!StringUtils.hasText(mainPageRequest.getLocation())) {
			throw new UserException("지역에 null 값이 들어갔습니다");
		}
		return LocationGroup.getLocations(mainPageRequest.getLocation());
	}
```

아래코드는 이전에 전략 패턴을 사용한 코드다

```
public List<Location> findLocation(MainPageForm mainPageRequest) {
		if (!StringUtils.hasText(mainPageRequest.getLocation())) {
			throw new UserException("지역에 null 값이 들어갔습니다");
		}
		LocationStrategy locationStrategy;
		String location = mainPageRequest.getLocation();
		switch (location) {
			case "북부":
				locationStrategy = new NorthLocation();
				break;
			case "동부":
				locationStrategy = new EastLocation();
				break;
			case "서부":
				locationStrategy = new WestLocation();
				break;
			case "남부":
				locationStrategy = new SouthLocation();
				break;
			case "전체":
				locationStrategy = new DefaultLocation();
				break;
			default:
				throw new UserException("카테고리의 제대로된 입력값을 넣어야 합니다");
		}
		return locationStrategy.getLocation();
	}
```

</details>

### (2) "메타 데이터" 빌더 패턴 적용

- 빌더패턴: “많은 인스턴스를 관리하는 역할”과 “해당 인스턴스를 생성하는 역할”을 만들어 기존 구조를 세부적(구체적)으로 분리시키는 패턴
- 메타 데이터 인스턴스를 관리하는 역햘은 MetaDataBuilder 인터페이스에게 역할 주었고 상황별 메타데이터를 생성하는 역할은 MetaDataDirector 클래스에게 역할을 부여하여 적용하였습니다

<img  alt="builderPatternExample" src="./img/builderPatternExample.png?raw=true">
<img  alt="metaDataPackage" src="./img/metaDataPackage.png?raw=true">

- 빌더 패턴을 사용한 이유: 새로운 메타 데이터가 생길때마다 list와 map을 사용하여 일일히 정보블 반환하는 것에 번거로움이 있었습니다 또한 메타데이터의 정보를 수정되는 경우도 다수 발생하는 것에 대비하여 위와
  같이 빌더 패턴을 적용하였습니다

### (3) "Service" Facade 패턴 적용

- Facade 패턴: "어떤 역할들의 집합"을 만들어 클라이언트 요청을 따로 “한번에” 관리하는 패턴입니다
- 본 프로젝트에서 Service 역할을 @Transaction 에 readOnly 옵션이 들어가는 곳과 들어가기 Service의 역할을 분리하였습니다. 그래서 @Transaction 에 readOnly 옵션 유뮤에
  따라 클래스를 분리하였으며, 해당 클래스들을 Facade 패턴을 사용하여 객체 관리하도록 하였습니다.

<img  alt="FavoriteServiceFacade" src="./img/FavoriteServiceFacade.png?raw=true">

<img  alt="FavoriteServiceSeperate" src="./img/FavoriteServiceSeperate.png?raw=true">

(성능 최적화 내용은 "프로젝트 종료 이후 혼자서 진행한 리팩토링"의 6. @Transaction 최적화에 있습니다)

- Facade 패턴을 사용한 이유: repository 특성(readonly 유무)별로 service를 분리하였지만, controller 에서 service 종류에 따라 호출하게 되면 변경사항이 있을시 수정할
  부분이 다수라 변경이 번거로웠습니다. 그래서 service 호출하는 곳을 한곳에 관리하여 controller가 "한 개의 service"에서 호출할 수 있도록 하여 유지보수를 편리하게 하였습니다.


