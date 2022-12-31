#!/bin/bash




echo "2222"
# 기존 이미지 삭제
sudo docker rmi bluelaw/jenkeinsfreestyle:1.0;

echo "3333"
sudo docker stop jenkeinsfreestyle

echo "4444"
sudo docker rm jenkeinsfreestyle

echo "5555"
sudo docker build -t bluelaw/jenkeinsfreestyle:1.0 .

echo "6666"
# 도커 run  -v /home/ec2-user:/config
sudo docker run -p 8080:8080 --name jenkeinsfreestyle bluelaw/jenkeinsfreestyle:1.0
# 여기서 -d 옵션을 주면 백그라운드에서 실행하는 거라서 spring 이 시작할때 나오는 로고.. 출력문들을 볼수 없다

echo "7777"



echo "Done!!!!!!!!!  feel good"


#
#
#echo "2222"
## 기존 이미지 삭제
#docker rmi bluelaw/jenkeinsfreestyle:1.0;
#
#echo "3333"
#docker stop jenkeinsfreestyle
#
#echo "4444"
#docker rm jenkeinsfreestyle
#
#echo "5555"
#docker build -t bluelaw/jenkeinsfreestyle:1.0 .
#
#echo "6666"
## 도커 run  -v /home/ec2-user:/config
#docker run -p 8080:8080 --name jenkeinsfreestyle bluelaw/jenkeinsfreestyle:1.0
## 여기서 -d 옵션을 주면 백그라운드에서 실행하는 거라서 spring 이 시작할때 나오는 로고.. 출력문들을 볼수 없다
#
#echo "7777"
##docker start jenkeinsfreestyle
##
##
##echo "8888"
## 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제 안됨
#
#
#
#echo "Done!!!!!!!!!  feel good"
