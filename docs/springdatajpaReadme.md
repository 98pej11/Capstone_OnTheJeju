## 2. Spring Data JPA

### (1) deleteAll 메서드

- spring Data JPA에서의 기본 deleteAll(entities) 메서드는 엔티티 하나마다 쿼리문을 날리데 되어서 속도가 많이 느립니다 이를 성능 개선 하기 위해 한번에 delete 연산을 하는 메서드를
  만들어 해결하였습니다.  
  <img alt="bulkDeleteMemberSpotByMember" src="./img/bulkDeleteMemberSpotByMember.png?raw=true"  width="800" height="180"/>

- 위에와 비슷하게 회원가입시 관광지와 연관되어 다수의 회원 정보를 업데이트를 해야하는 경우가 있었는데 처음에는 entity 생성마다 spring data jpa의 save()메서들 사용하여 하나씩 저장하였는데
  성능이 너무 나오지 않았다 그래서 for loop로 하나씩 save하는 것 보단 List에 entity를 전부 담아서 한 번의 saveAll이 더 성능에 좋은 것을 알게 되어 saveAllAndFlush()를
  사용하여 선능 튜닝을 해결하였습니다

  <img  alt="saveAllAndFlush" src="./img/saveAllAndFlush.png?raw=true"  width="1000" height="250"/>
