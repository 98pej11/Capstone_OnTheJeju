# Capstone-JejuTourRecommend


이 본 프로젝트는 사용자 맞춤 제주도 관광지 추천 플랫폼입니다

제주도 관광시, 주요 평가요소 4가지를 추려 조사하였고, 그에 따라 관광지 리스트를 보여줍니다

관광지 선호도 판단은 네이버와 카카오 댓글들 기반으로 ai를 적용하였으며 
관광지 상세 페이지에 뷰, 가격, 시설, 카페및주변시설 로 카테고리를 나누어 수치화 하여 보여줍니다

저희가 만든 ai로 사용자에게 4가지 맞춤 서비스를 제공합니다.

첫번째로, 메인 페이지에서 제주도를 동서남북으로 나눠, 검색할 수 있도록 하였으며 뷰, 가격 , 주변시설, 서비스 총 4가지 카테고리에 맞춰 수치화된 점수 기반으로 관광지를 순위대로 보여줍니다

2번째는 만약 사용자가 카테고리별 세부 설정을 하고 싶으면, 우선순위 버튼으로 각 카테고리에 가중치를 주어, 이에 따라 관광지 순위를 보여줍니다.

3번째는 관광지 상세페이지에서 관광지의 상세점수를 볼 수 있게 하였습니다

4번째는 찜하기 기능으로, 사용자 만의 관광지 리스트를 만들었을 경우, 해당 관광지의 최단 경로를 보여줍니다. 
또한 사용자의 각 위시리스트를 분석하여, 사용자 성향을 파악해 여행경로 주변 관광지를 추천해줍니다.

실제 구현 코드는 jejuTour-recommend를 보시며 됩니다
JejuTourRecommend는 테스트용 코드입니다
 
기술 스택으로는 spring-boot, jpa, querydsl로 백엔드를 구성하였습니다.
서버배포는 ec2 로 하였으며, 데이터베이스는 mysql로 진행하였습니다.
그러나 위에 코드는 지속적인 빠른 테스트를 h2디비를 사용하였습니다.


전반적인 자세한 소개와 시현영상은 아래 링크로 들어가면 자세히 볼수 있습니다.
<소개 영상 링크>
https://blog.naver.com/PostView.naver?blogId=suheonj95&Redirect=View&logNo=222783108548&categoryNo=1&isAfterWrite=true&isMrblogPost=false&isHappyBeanLeverage=true&contentLength=5077&isWeeklyDiaryPopupEnabled=true



<메인 페이지>

<img width="1920" alt="메인페이지" src="https://user-images.githubusercontent.com/23393574/174818259-60db8349-8c55-487e-97ee-817f240b6a56.png">


<테이블 구성도>

<img width="805" alt="테이블 구성도" src="https://user-images.githubusercontent.com/23393574/174819516-2381636f-06c8-40db-90ef-9db7efeb19fd.png">
















