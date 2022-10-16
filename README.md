# :runner: Capstone-JejuTourRecommend

# :star2: 소개 영상 및 설명

<details>
  
<summary> 본문 확인 (👈 Click)</summary>

- 아래 블로그를 통해 자세한 내용을 확인할 수 있습니다

   https://blog.naver.com/PostView.naver?blogId=suheonj95&Redirect=View&logNo=222783108548&categoryNo=1&isAfterWrite=true&isMrblogPost=false&isHappyBeanLeverage=true&contentLength=5077&isWeeklyDiaryPopupEnabled=true

</details>

# :package: 역할

<details>

<summary> 본문 확인 (👈 Click)</summary>

<br>

|백엔드|프런트엔드|AI|배포, 크롤링|
|:---:|:---:|:---:|:---:|
| - 주수헌(팀장, 본인) <br> [GitHub](https://github.com/suheonjoo) <br> | - 정세연 <br> [GitHub](https://github.com/n0eyes) <br> | - 박은정 <br> [GitHub](https://github.com/98pej11) <br> |@정진찬|


### Languages

<img alt="HTML5" src ="https://img.shields.io/badge/HTML5-E34F26.svg?&style=for-the-badge&logo=HTML5&logoColor=white"/>
<img alt="CSS3" src ="https://img.shields.io/badge/CSS3-1572B6.svg?&style=for-the-badge&logo=CSS3&logoColor=white"/>
<img alt="JavaScript" src ="https://img.shields.io/badge/JavaScript-F7DF1E.svg?&style=for-the-badge&logo=JavaScript&logoColor=white"/>
<img alt="TypeScript" src ="https://img.shields.io/badge/TypeScript-3178C6.svg?&style=for-the-badge&logo=TypeScript&logoColor=white"/>
<img alt="Java" src ="https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white"/>

### Technologies
<img alt="Git" src ="https://img.shields.io/badge/Git-F05032.svg?&style=for-the-badge&logo=Git&logoColor=white"/> 
<img alt="GitLab" src ="https://img.shields.io/badge/GitLab-FCA121.svg?&style=for-the-badge&logo=GitLab&logoColor=white"/>
<img alt="AWS" src ="https://img.shields.io/badge/AWS-232F3E.svg?&style=for-the-badge&logo=amazonaws&logoColor=white"/>
<img alt="Python" src ="https://img.shields.io/badge/Python-3178C6.svg?&style=for-the-badge&logo=Python&logoColor=white"/>
<!--<img alt="Linux" src ="https://img.shields.io/badge/Linux-FCC624.svg?&style=for-the-badge&logo=linux&logoColor=white"/>-->
<!--<img alt="Jenkins" src ="https://img.shields.io/badge/Jenkins-D24939.svg?&style=for-the-badge&logo=Jenkins&logoColor=white"/>-->
<!--<img alt="Docker" src ="https://img.shields.io/badge/Docker-4479A1.svg?&style=for-the-badge&logo=Docker&logoColor=white"/>-->
<img alt="React" src ="https://img.shields.io/badge/React-61DAFB.svg?&style=for-the-badge&logo=React&logoColor=white"/>
<!--<img alt="FCM" src ="https://img.shields.io/badge/FCM-FFCA28.svg?&style=for-the-badge&logo=firebase&logoColor=white"/>-->

<img alt="Spring Boot" src ="https://img.shields.io/badge/Spring Boot-6DB33F.svg?&style=for-the-badge&logo=springboot&logoColor=white"/>
<!--<img alt="Spring Batch" src ="https://img.shields.io/badge/Spring Batch-6DB33F.svg?&style=for-the-badge&logo=springbatch&logoColor=white"/>-->
<img alt="JPA" src ="https://img.shields.io/badge/jpa-6DB33F.svg?&style=for-the-badge&logo=jpa&logoColor=white"/>
<img alt="queryDsl" src ="https://img.shields.io/badge/querydsl-4479A1.svg?&style=for-the-badge&logo=querydsl&logoColor=white"/>
<img alt="mysql" src ="https://img.shields.io/badge/mysql-4479A1.svg?&style=for-the-badge&logo=mysql&logoColor=white"/>
<img alt="Redis" src ="https://img.shields.io/badge/Redis-DC382D.svg?&style=for-the-badge&logo=redis&logoColor=white"/>



</details>

# :zap: 성능 개선

<details>
<summary> 본문 확인 (👈 Click)</summary>
  
## 1. Querydsl

### (1) 묵시적 조인을 모두 명시적 조인으로 수정

- 기존에 묵시적 조인으로 상세한 조인 명령을 하지 않았더니 원하던 InnerJoin으로 쿼리문이 나가지 않고 Cross join으로 쿼리문이 나가는 것을 확인하여 수정하였습니다

### (2) exit 함수 수정

- JPQL에서 select의 exists 를 지원하지 않습니다 (select exists 문법)
  (단, where의 exists는 지원합니다)
  ->그래서 exists 를 우회하기 위해 count 쿼리를 사용합니다 -> 이때 문제가 생깁니다

- querydsl의 exist는 실제로 성능이슈가 있는 count()>0으로 실행됩니다
  (Querydsl에서 기본적으로 지원하는 exists 를 보면 성능상 이슈가 있는 count 쿼리 방식을 사용했습니다)
  count는 전체 다 훑어보는 것으로 성능 저하 문제가 생깁니다

- 해결방법
  limit(1)을 사용하여 해결하였습니다
  jpql에서는 from없이는 쿼리가 실행되지 않아서 limit(1)을 사용하였습니다
  limit(1)로 조회제한을 한여 실행하였습니다 (= fetchFirst())

<img  alt="querydslExitUpgrade" src="./images/querydslExitUpgrade.png?raw=true"  >



### (3) 서브쿼리 -> 조인, 쿼리분할

- querydsl에서 서브쿼리는 안티패턴인 것을 확인하여, 조인을 사용하고, 쿼리 분할을 하여 서브쿼리를 대체하였습니다.

## 2. Spring Data JPA

### (1) deleteAll 메서드

- spring Data JPA에서의 기본 deleteAll(entities) 메서드는 엔티티 하나마다 쿼리문을 날리데 되어서 속도가 많이 느립니다
  이를 성능 개선 하기 위해 한번에 delete 연산을 하는 메서드를 만들어 해결하였습니다.
  <img alt="bulkDeleteMemberSpotByMember" src="./images/bulkDeleteMemberSpotByMember.png?raw=true"  width="800" height="180"/>

- 위에와 비슷하게 회원가입시 관광지와 연관되어 다수의 회원 정보를 업데이트를 해야하는 경우가 있었는데 처음에는 entity 생성마다
  spring data jpa의 save()메서들 사용하여 하나씩 저장하였는데 성능이 너무 나오지 않았다
  그래서 for loop로 하나씩 save하는 것 보단 List에 entity를 전부 담아서 한 번의 saveAll이 더 성능에 좋은 것을 알게 되어 saveAllAndFlush()를 사용하여 선능 튜닝을 해결하였습니다
  <img  alt="saveAllAndFlush" src="./images/saveAllAndFlush.png?raw=true"  width="1000" height="250"/>

</details>

# :recycle: 프로젝트 종료 이후 혼자서 진행한 리팩토링

<details>

  <summary> 본문 확인 (👈 Click)</summary>

## 1. API 명세서 수정

본 프로젝트가 종료 되고 주변 지인 그리고 발표 영상 평가 및 심사위원님들의 피드백을 듣고 사용자 측면에서 더 편리한 UI를 고려하여 기존 API 명세서 내용을 수정하였습니다

### (1) 메인페이지에서 사용가 찜했던 관광지도 표시할수 있게 수정

- 사용자가 기존에 위시리스트에 관광지를 추가했는지 알수있게 표시하도록 하였습니다

### (2) 메인 페이지에서 사진 노출 1장 -> 3장

- 기존 메인 페이지에서 관광지별 사진에 마우스 커서를 갖다대면, 설명이 나오게 했습니다. 거기에서 추가로 사진 한장이 아닌 여러장을 볼수 있게 api를 수정하였습니다.

### (3) 위시리시트페이지 사진 노출 1장 -> 3장

- 기존에 위시리스트 페이지에서 위시리스트 화면의 사진을 대표 사진 한장으로 대체 하였으나 여러장으로 보여줄수 있게 하였습니다.

## 2. 객체지항의 오해와 사실, 디자인 패턴 적용

- “객체 지향의 사실과 오해” 책을 통해 객체 지향의 의미를 좀 더 이해할 수 있는 계기 되었습니다. 그래서 "객체 지향 언어인 자바"를 책에서 말한 역할, 책임, 협력의 관점으로 바라보며 설계할 수 있다는 것을 알게 되었습니다.
  이후 "객체 지향의 역할, 책임, 협력"을 23가지 패턴으로 만든 “GOF의 23가지 디자인 패턴”도 학습하여 본 프로젝트에 적용하여 좀더 객체 지향적인 코드로 바꾸었습니다

([디자인 패턴 학습 내용 링크](https://github.com/suheonjoo/Study-Document/tree/master/%EB%94%94%EC%9E%90%EC%9D%B8%20%ED%8C%A8%ED%84%B4%20%EC%A0%95%EB%A6%AC))


### (1) "관광지 위치" 전략 패턴 적용

- 전략 패턴: “상황내용을 포함하는(가지고 있는) 역할”과 “상황에 따른 다양한 전략을 포함하는 역할”을 나누어 전략들을 분리하는 패턴을 만들었습니다
  저는 동서남북의 클래스를 따로 분리하여 "위치 정보를 가지고 있는 역할"을 만들고,
  이러한 "위치 정보를 관리하는 역할" LocationStrategy 인터페이스를 만들어 객체들간의 협력 관계를 만들었습니다

<img  alt="stragetyPatternPackage" src="./images/stragetyPatternPackage.png?raw=true"  >
<img  alt="stragetyPatternExample" src="./images/stragetyPatternExample.png?raw=true"  >

- 전략 패턴을 사용한 이유: 현재 동서남북으로 위치정보를 분리하 것은 설문조사와 각 읍별 관광지의 개수를 고려하여 저희 임의의 적절한 지억을 나누었습니다.
  이는 관광지가 새로 생길수 있어 지역별 관광지 개수 변경이 되는 우려가 있었습니다
  그래새 유지보수를 더 편리하게 하기 위해서 전략 패턴을 적용하였습니다.

### (2) "메타 데이터" 빌더 패턴 적용

- 빌더패턴: “많은 인스턴스를 관리하는 역할”과 “해당 인스턴스를 생성하는 역할”을 만들어 기존 구조를 세부적(구체적)으로 분리시키는 패턴
- 메타 데이터 인스턴스를 관리하는 역햘은 MetaDataBuilder 인터페이스에게 역할 주었고
  상황별 메타데이터를 생성하는 역할은 MetaDataDirector 클래스에게 역할을 부여하여 적용하였습니다

<img  alt="builderPatternExample" src="./images/builderPatternExample.png?raw=true">
<img  alt="metaDataPackage" src="./images/metaDataPackage.png?raw=true">

- 빌더 패턴을 사용한 이유: 새로운 메타 데이터가 생길때마다 list와 map을 사용하여 일일히 정보블 반환하는 것에 번거로움이 있었습니다
  또한 메타데이터의 정보를 수정되는 경우도 다수 발생하는 것에 대비하여 위와 같이 빌더 패턴을 적용하였습니다

### (3) "Service" Facade 패턴 적용
- Facade 패턴: "어떤 역할들의 집합"을 만들어 클라이언트 요청을 따로 “한번에” 관리하는 패턴입니다
- 본 프로젝트에서 Service 역할을 @Transaction 에 readOnly 옵션이 들어가는 곳과 들어가기 Service의 역할을 분리하였습니다.
그래서 @Transaction 에 readOnly 옵션 유뮤에 따라 클래스를 분리하였으며, 해당 클래스들을 Facade 패턴을 사용하여 객체 관리하도록 하였습니다.

<img  alt="FavoriteServiceFacade" src="./images/FavoriteServiceFacade.png?raw=true">

<img  alt="FavoriteServiceSeperate" src="./images/FavoriteServiceSeperate.png?raw=true">

(성능 최적화 내용은 "프로젝트 종료 이후 혼자서 진행한 리팩토링"의 6. @Transaction 최적화에 있습니다)

- Facade 패턴을 사용한 이유: repository 특성(readonly 유무)별로 service를 분리하였지만, controller 에서 service 종류에 따라 호출하게 되면 변경사항이 있을시 수정할 부분이 다수라 변경이 번거로웠습니다. 
그래서 service 호출하는 곳을 한곳에 관리하여 controller가 "한 개의 service"에서 호출할 수 있도록 하여 유지보수를 편리하게 하였습니다.

## 3. Spring Security 개선

### (1) Spring Security 구조 개선

- 스프링과 JPA를 학습한지 3주만에 프로젝트를 들어간 상황 이었기에 Spring Security는 제대로 모른 상태로 본 프로젝트에 들어갔습니다.
  프로젝트가 종료이후 Spring Security를 학습하여 기존에 엉망이었던 코드 내용들을 수정 작업하였습니다.

### (2) redis 데이터베이스 추가

- logoutToken는 redis 데이터베이스를 새로 적용하여 토큰 정보를 가져오도록 하여 성능 개선을 했습니다.


## 4. CI/CD

- 본 프로젝트 진행할 당시, 배포를 담당하지 않았습니다. 이후 프로젝트가 종료우 제가 따로 AWS, Deploy, GitHubAction으로 자동 배포가 되도록 하였습니다.

## 5. DDD 설계 (진행 중)

- 이전에 프로젝트는 mvc 패턴으로 repository, service, controller 만으로 나누어 설계를 하였습니다. 그런데 프로젝트가 요구사항이 많아질수록 
 repository, service, controller 들이 각각 점점 커지게 되면서 원하는 클래스를 찾기가 힘들었습니다. 즉, 유지 보수가 어려웠습니다. 그래서 이에 대한 해결방법을 
알아보던중 도메인 주도 설계(DDD)를 알게 되어 적용해 보는 중입니다. 


## 6. @Transaction 최적화

- readOnly=true 옵션을 사용하면 읽기 전용 트랜잭션이 생성됩니다.
  (readOnly 는 사용하면 말그대로 읽지 전용이라, 데이터 변경이 일어나지 않을 때 사용하는 트랜잭션 애노테이션입니다.)

- JPA(하이버네이트)는 읽기 전용 트랜잭션의 경우 커밋 시점에 플러시를 호출하지 않습니다. 읽기 전용이니 변경에 사용되는 플러시(em.flush: 영속성 컨텍스트에 있는 것을 디비에 저장)를 호출할 필요가 없습니다.
추가로 변경이 필요 없으니 변경 감지를 위한 스냅샷 객체도 생성하지 않습니다.

- readOnly 옵셥으로 선능을 최적화 할수 있다는 것을 알고 데이터를 읽기만하는 Service 같은 경우 CommandUseCase, QueryUseCase 분리하여 reaOnly 옵션을 처리하였습니다.
그 후 디자인 패턴인 Facade 패턴을 사용하여 Service 를 한곳에 관리하는 역할을 만들어 분리하였습니다.
  

</details>
