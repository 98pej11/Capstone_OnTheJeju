## enum 타입의 재발견

시작은 우테코 프리코스에서 enum을 사용하게 되면서, 생각보다 enum 활용방안이 무궁무진할것 같아 학습하기 시작하였다.

결론은: 1대1 대응관계 있는 집합덩어리라고 생각하게 되었다. 이렇게 결론만 놓고 보면 클래스도 여러 집합을 만들어서 관리할수 있다고 하는데 내가 생각하는 enum 장점은 아래와 같다

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

아래 코드는 최종 서비스 영역에서 개선된 코드이다

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



