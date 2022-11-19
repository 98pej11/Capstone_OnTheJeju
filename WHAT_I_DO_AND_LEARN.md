# 알게된 사실들

## 1. OSIV = false 로 설정으로 인한 에러

- -> 대부분의 reference 에서는 비지니스 계층에서의 트랜잿션 그림 그려져 있었으나, view 에서 가져온 영속성 컨텍스트 또한 조회만 가능하여 수정 작업을 하려면 다시 리포지토리에서 해당 엔티티를
  가져와야한다
    - 결론: 비지니스 영역에서의 영속성 컨텍스트 내용은 뷰에서 수정 작업을 하면 안됬을 뿐만 아니라, 뷰 영역에서의 영속성 컨텍스트내용도 비지니스 영역에서 수정작업을 하면 안된다

## 2. 인덱싱

본 프로젝트에서 picture 테이블에서 spotId 기준으로 조회하는 쿼리가 대다수 이므로 이 것의 성능 최적화 할수 있는 방법이 있나 고민을 하였다 그래서 인덱싱 이란 개념을 알게 되어 ~~(정말 힘들게
읽은)~~real MySQL 8.0 책에서 인덱싱 내용을 들여다 보았다 ~~(처음 인덱싱 내용을 봤을때 이해가 잘됐음 ㅠ)~~
우선 기본적으로 innodb 엔진 특성상 picture id 는 picture 의 unique 한값을 클러스터 인덱싱이 기본적으로 되어 있다. 그래서 난 추가적으로 spotId를 인덱싱에 추가하여 spotId,
pictureId 순으로 복합 인덱싱을 하려고 하였고 그 조회 결과는 다음과 같았다

![](./imgW/spotId_pictureId_indexEx1.png)

위 사진의 extra 는 Using index Condition, type 은 range 으로 인덱싱을 잘 잡아준 것을 볼수 있었다

![](./imgW/spotId_pictureId_indexEx2.png)

그러나 인덱싱 하기 전의 실행계획을 첫번째 사진과 같이 extra 는 Using index Condition, type 은 range 으로 인덱싱을 잘 잡아준 것을 볼수 있었다 그래서 인덱싱을 주기 전의 인덱싱을
조회 해보니 아래 사진과 같이 pictureId, spotId 로 인덱싱이 이미 있었다

![](./imgW/spotId_pictureId_indexEx3.png)

결론: 따로 사용자가 인덱싱을 잡아 주지 않아도 mysql 에서는 primary key 와 foreign key 로 이미 인뎅싱을 잡아 준다

# 내가 처리한 것

## JPA n+1 어떻게 처리했나?

1. 패치조인을 사용함, where in절 쿼리 내용이 적은 경우라. 한번에 가져와서 어플리케이션에서 filter하는게 맞다고 판단함
    - -> 한번에 다 조회해서 dto에 각각 담음
        - 2-1번하고 차이점은 이 service에 반환하는 api가 페이징dto가 아니여서 페치조인함, 페이징 컬렉션 페이징은 못하니깐

2. n+1 발생되는 테이블에서 조회를 하고 dto에 붙이는 형식으로 함 이때 여기서
    1. where 의 in절 조건 리스트가 많지 않는 경우: 그냥 디비에서 꺼내서 어플리케이션 영역에서 collection 자료구조의 groupby, limit를 사용해서 붙이는 형식으로 했음,한번에 많은 양을
       디비에서 가져오는것은 리소스 과부하라고 생각
        - -> 어플리케이션에서 groupby, limit
    2. in절 조건에 리스트사이즈가 큰 경우:  디비에서 groupby, limit 로 처리함 -> db에서 groupby , limit c
        - 이후 real mysql 실행계획을 어떻게 보고 인덱싱 처리 유무를 어떻게 판단하는지 볼려고 구글링 엄청함 보니깐 intellij에 유용한 기능 엄청 많음.
            - 이걸로 실행계획 보고 드디어 real mysql 이 이해가기 시작했음 -> 진짜 백과 사전같이 볼수 있었음
                - ~~(물론 처음볼때 이해가 안갔는데 어떻게든 정리하면서 한번 정독한게 도움 되었기에 가능한거 )~~

## 프로젝트 구조

[프로젝트 구조 링크](./docs/projectStructure.md)

## enum 타입의 재발견

[enum 타입의 재발견 링크](./docs/enumDocs.md)


















