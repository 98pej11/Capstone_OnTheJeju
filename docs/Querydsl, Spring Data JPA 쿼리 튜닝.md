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

- 해결방법 limit(1)을 사용하여 해결하였습니다 jpql에서는 from없이는 쿼리가 실행되지 않아서 limit(1)을 사용하였습니다 limit(1)로 조회제한을 한여 실행하였습니다 (= fetchFirst())

<img  alt="querydslExitUpgrade" src="./img/querydslExitUpgrade.png?raw=true"  >

### (3) 서브쿼리 -> 조인, 쿼리분할

- mysql 5.5 버전의 서브 쿼리를 EXPLAIN 명령으로 실행 계획을 보면 "DEPENDENT SUBQUERY"로 줄력이 됩니다. 즉 먼저 서브쿼리를 실행하고 상위쿼리를 실행하는 것이 아니라 상위쿼리 데이터를
  모두 가져오고 서브쿼리를 실행합니다. -> 이럴때 성능 문제가 발생합니다
- 그러나 mysql 5.6 버전부터 서브쿼리의 성능 문제가 대폭 최적화 되어 이러한 문제가 개선 되었습니다.
- 현재 저희 프로젝트는 mysql 8.0 버전을 사용하여 서브쿼리에 대한 성능 이슈에 대한 문제가 거의 없지만, 버전간의 호환성문제를 제거하기 위해 서브쿼리를 조인 또는 쿼리분할로 대체하였습니다.

### (4) 컬렉션 조인 최적화

- 일대다 관계를 맺은 컬렉션 페치 조인의 경우, n+1문제가 발생하게 됩니다. 그러나 페치조인 같은 경우 페이징을 할수 없습니다.
    1. 방법1 :  그래서 저는 루트 데이터 1번에 컬렉션 데이터 n번의 컬렉션 데이터를 가져오는 방식을 해결하였으나 이 또한 데이터를 가져오는 데 시간이 오래 걸렸습니다.
    2. 방법2 : 방법1 문제를 해결하고자 ToOne 관계들을 먼저 조회하고, ToOne관계에서 얻은 "식별자 관광지 아이디"로 ToMany 관계인 사진 URL 한꺼번에 조회하였습니다.
        - 그리고 map을 사용하여 관광지 아이디를 매칭하였습니다. -> 그러자 성능 향상이 O(1)로 줄어들게 되었습니다.
        - 이렇게 접근하니, 수만 개의 데이터를 가져오려 해도 전보다 2배의 성능 개선을 할 수 있었습니다


## 2. Spring Data JPA

### deleteAll 메서드

- spring Data JPA에서의 기본 deleteAll(entities) 메서드는 엔티티 하나마다 쿼리문을 날리데 되어서 속도가 많이 느립니다 이를 성능 개선 하기 위해 한번에 delete 연산을 하는 메서드를
  만들어 해결하였습니다.  
  <img alt="bulkDeleteMemberSpotByMember" src="./img/bulkDeleteMemberSpotByMember.png?raw=true"  width="800" height="180"/>

- 위에와 비슷하게 회원가입시 관광지와 연관되어 다수의 회원 정보를 업데이트를 해야하는 경우가 있었는데 처음에는 entity 생성마다 spring data jpa의 save()메서들 사용하여 하나씩 저장하였는데
  성능이 너무 나오지 않았다 그래서 for loop로 하나씩 save하는 것 보단 List에 entity를 전부 담아서 한 번의 saveAll이 더 성능에 좋은 것을 알게 되어 saveAllAndFlush()를
  사용하여 선능 튜닝을 해결하였습니다

  <img  alt="saveAllAndFlush" src="./img/saveAllAndFlush.png?raw=true"  width="1000" height="250"/>







