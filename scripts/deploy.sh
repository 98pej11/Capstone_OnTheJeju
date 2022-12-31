#!/bin/bash




# 가동중인 awsstudy 도커 중단 및 삭제
echo "11111111"
docker ps -a -q --filter "name=jenkeinsfreestyle" | grep -q . && docker stop jenkeinsfreestyle && docker rm jenkeinsfreestyle | true


echo "3333"
# 기존 이미지 삭제
docker rmi bluelaw/jenkeinsfreestyle:1.0;

echo "4444"
docker rm jenkeinsfreestyle

echo "5555"
docker build -t bluelaw/jenkeinsfreestyle:1.0 .

echo "6666"
# 도커 run  -v /home/ec2-user:/config
docker run -d -p 8080:8080 --name jenkeinsfreestyle bluelaw/jenkeinsfreestyle:1.0


echo "7777"
docker start jenkeinsfreestyle


echo "8888"
# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제 안됨



echo "Done!!!!!!!!!  feel good"
