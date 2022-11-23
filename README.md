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
<img alt="Python" src ="https://img.shields.io/badge/Python-3178C6.svg?&style=for-the-badge&logo=Python&logoColor=white"/>
<img alt="Java" src ="https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white"/>

### Technologies

<img alt="Git" src ="https://img.shields.io/badge/Git-F05032.svg?&style=for-the-badge&logo=Git&logoColor=white"/> 
<!--<img alt="GitLab" src ="https://img.shields.io/badge/GitLab-FCA121.svg?&style=for-the-badge&logo=GitLab&logoColor=white"/>-->
<img alt="AWS" src ="https://img.shields.io/badge/AWS-232F3E.svg?&style=for-the-badge&logo=amazonaws&logoColor=white"/>
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

# WHAT I KNOW AMD LEARN

<details>
<summary> 본문 확인 (👈 Click)</summary>

현재 계속 프로젝트를 진행하면서 성능개선 외에서 "알게된 사실"과 "프로젝트에서 적용한 내용"을 기록하였다

[WHAT_I_DO_AND_LEARN](./WHAT_I_DO_AND_LEARN.md)

</details>

# :zap: 성능 개선

<details>
<summary> 본문 확인 (👈 Click)</summary>

## 1. Querydsl

[querydsl 리드미 링크](./docs/querydslReadme.md)

## 2. Spring Data JPA

[springdataJpa 리드미 링크](./docs/springdatajpaReadme.md)

[//]: # ()

[//]: # (## 3. 인덱싱)

[//]: # ()

[//]: # (- 인덱싱을 통해 쿼리 튜닝이 가능하다고 하여, 인데싱 관련 서적, real mysql 8.0을 사서 학습하였다. )


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

- “객체 지향의 사실과 오해” 책을 통해 객체 지향의 의미를 좀 더 이해할 수 있는 계기 되었습니다. 그래서 "객체 지향 언어인 자바"를 책에서 말한 역할, 책임, 협력의 관점으로 바라보며 설계할 수 있다는 것을
  알게 되었습니다. 이후 "객체 지향의 역할, 책임, 협력"을 23가지 패턴으로 만든 “GOF의 23가지 디자인 패턴”도 학습하여 본 프로젝트에 적용하여 좀더 객체 지향적인 코드로 바꾸었습니다

([디자인 패턴 학습 내용 링크](https://github.com/suheonjoo/Study-Document/tree/master/%EB%94%94%EC%9E%90%EC%9D%B8%20%ED%8C%A8%ED%84%B4%20%EC%A0%95%EB%A6%AC))

[객체지항의 오해와 사실, 디자인 패턴 적용 리드미 링크](./docs/oopAndDesignPattern.md)

## 3. Spring Security 개선

### (1) Spring Security 구조 개선

- 스프링과 JPA를 학습한지 3주만에 프로젝트를 들어간 상황 이었기에 Spring Security는 제대로 모른 상태로 본 프로젝트에 들어갔습니다.
    - 프로젝트가 종료이후 Spring Security를 학습하여 기존에 엉망이었던 코드 내용들을 수정 작업하였습니다.
- 기존에 spring Security의 인증 인가 처리를 제가 만든 개별 filter에 모든 것을 처리하였습니다.
    - Spring security 학습 후,
    - filter (CustomLoginProcessingAuthenticationFilter)
    - handler(CustomAuthenticationFailureHandler, CustomAuthenticationSuccessHandler, RestAccessDeniedHandler)
    - provider(RestAuthenticationEntryPoint, CustomAuthenticationProvider)를 만들어, security 라이브러리 클래스 목적에 맞게 implement 하여
      재정의하였습니다.

[스프링 security 패지키 구조 설명 및 개선 사항 링크](./docs/SpringSecuriyImprovement.md)


### (2) redis 데이터베이스 추가

- logoutToken는 redis 데이터베이스를 새로 적용하여 토큰 정보를 가져오도록 하여 성능 개선을 했습니다.

## 4. CI/CD

- 본 프로젝트 진행할 당시, 배포를 담당하지 않았습니다. 이후 프로젝트가 종료우 제가 따로 AWS, Deploy, GitHubAction으로 자동 배포가 되도록 하였습니다.

## 5. DDD 설계 (진행 중)

- 이전에 프로젝트는 mvc 패턴으로 repository, service, controller 만으로 나누어 설계를 하였습니다. 그런데 프로젝트가 요구사항이 많아질수록 repository, service,
  controller 들이 각각 점점 커지게 되면서 원하는 클래스를 찾기가 힘들었습니다. 즉, 유지 보수가 어려웠습니다. 그래서 이에 대한 해결방법을 알아보던중 도메인 주도 설계(DDD)를 알게 되어 적용해 보는
  중입니다.

## 6. @Transaction 최적화

- readOnly=true 옵션을 사용하면 읽기 전용 트랜잭션이 생성됩니다.
  (readOnly 는 사용하면 말그대로 읽지 전용이라, 데이터 변경이 일어나지 않을 때 사용하는 트랜잭션 애노테이션입니다.)

- JPA(하이버네이트)는 읽기 전용 트랜잭션의 경우 커밋 시점에 플러시를 호출하지 않습니다. 읽기 전용이니 변경에 사용되는 플러시(em.flush: 영속성 컨텍스트에 있는 것을 디비에 저장)를 호출할 필요가
  없습니다. 추가로 변경이 필요 없으니 변경 감지를 위한 스냅샷 객체도 생성하지 않습니다.

- readOnly 옵셥으로 선능을 최적화 할수 있다는 것을 알고 데이터를 읽기만하는 Service 같은 경우 CommandUseCase, QueryUseCase 분리하여 reaOnly 옵션을 처리하였습니다. 그 후
  디자인 패턴인 Facade 패턴을 사용하여 Service 를 한곳에 관리하는 역할을 만들어 분리하였습니다.

<!--
## 7. 쿼리 튜닝

- -->


</details>
