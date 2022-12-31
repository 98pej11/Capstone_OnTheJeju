#!/bin/bash




# 가동중인 awsstudy 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=jenkeinsfreestyle" | \
grep -q . && docker stop jenkeinsfreestyle && docker rm jenkeinsfreestyle | true

# 기존 이미지 삭제
sudo docker rmi bluelaw/jenkeinsfreestyle:1.0;

docker rm jenkeinsfreestyle

docker build -t bluelaw/jenkeinsfreestyle:1.0 .

# 도커 run  -v /home/ec2-user:/config
docker run -d -p 8080:8080 --name jenkeinsfreestyle bluelaw/jenkeinsfreestyle:1.0

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제 안됨
docker rmi -f $(docker images -f "dangling=true" -q) || true


echo "Done!!!!!!!!!  feel good"
