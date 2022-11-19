## 프로젝트 구조

1. wishlist 패키지의 도메인 서비스 영역에서는 CommandUseCase와 QueryUseCase를 분리했는데, spot.main 패키지에서는 이 둘을 인터페이스로만 분리해 놓고 공용 서비스 구현체를 사용한
   이유?
    - SpotListService에서의 관련 메서드들은 각각 2개 1개로 작았으며, 두 인터페이스가 공용을 사용하는 메서드가 있었기에 합치는 것이 맞다고 생각하였다
    - 만약 CommandUseCase와 QueryUseCase 의 메서드들이 많았다고 하면 따로 구현체를 분리 했을 것이며 공용 메서드를 처리하는 클래스를 따로 만들었을 것이다

2. 패키지를 spot, whishlist 로 나눈 이유는?
    - 원래 초기 Ver.1 프로젝트에서는 service, domain, repository, controller 패키지로만 나누었다. 그러나 프로젝트가 거대해지고 클랜 코드를 지키면서 프로젝트 리팩토링을
      진행해보니 한 패키지지에 많은 클래스와 인터페이스 덩어리가 있었다.
    - 이는 프로젝트를 처음부터 설계한 본인도 프로젝트를 개선시 관련 클래스, 인터페이스들을 찾기가 어려웠으면 구조 수정이 필요하다고 생각하였다
    - 그래서 패지키의 큰틀을 먼저 나누어야 겠다고 생각하여 관광지에 초첨이 더 맞춰져 있는 클래스들을 spot으로 두었고
    - 관광지보다 사용자 찜하기 목록에 관련된 클래스들은 whishlist 패키지로 나눠었다
    - 물론 이렇게 분리하고 공통으로 사용된 클래스들을 어디에다가 놓아야 할지 계속 고민중이다.
        - 일단 현 상황에서는 프로젝트 클래스 이름이 Favorite 과 같은 사용자 입장과 가까운 클래스들은 whishList 패키지에 나누었고,
        - 관광지 정보, 사진, 리뷰 와 같이 관광지의 정보에 추점을 둔 클래스들은 spot 패키지에 두었다.

3. spot 패키지 안에도 mainspot, detailspot 은 어떤 기준으로 나눈 것인가?
    - 기존 ver.1 에서처럼 해당 피키지 내부를 service, domain, repository, controller 으로만 나눌려고 했으나, spot 패키지의 같은 경우 앞전 2번에서처럼 패키지를 나누어
      관련 클래스들이 거대 했다,
        1. 그래서 더 디테일적으로 메인 페이지 관련된 mainSpot
        2. 관광지 세부 페이지와 관련된 DetailSpot 으로 패키지를 나누었다


4. 큰틀을 spot, whishList 로 패키지를 나누고 그안에 세부적으로 application, domain, infrastructure, presentation으로 나눈 이유는?
    - 기존 ver.1 이후 리팩토링 진행 후, controller, service, repository 내에서도 세부적으로 역할들을 나누다 보니 프로젝트가 거대해졌다. 그러다 보니 controller,
      service, repository 중에 하나만 고치게 되면 연관된 클래스들을 변경해야하는 것이 많았다
    - 이러한 변경에 유용하게 대응하기 위해 **패키지 또한 응집력의 필요성이 생겨** 각 패키지의 정보를 한개의 인페이스를 통해 소통하는 것의 필요성을 느꼈고 아래 와 같이 패키지를 나누었다.
        1. application 은 damain.service 를 컨트롤러가 직접적으로 호출하기 보다는 중간에 인터페이스를 만들어 controller 가 serivce 변경을 유연하게 대체 하기 위해 만든
           패키지이다
        2. domain 은 entity와 해당 entity 들이 담당하는 역할들을 모아 놓은 곳이다
            - 내부에 domain.repository 패키지도 있는데 이는 리포지토리 구현체를 한번에 받는 인터페이스를 만들어 infrastructure.repository 패키지의 변경을 유연하게
              대처하기 위해 만들었다
        3. infrastructure 는 오직 repository 구현체와 인터페이스들만 있는 패키지이다
        4. presentation 은 controller 역할을 하는 곳이다 

