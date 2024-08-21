#!/bin/bash

# 定義變數
DOCKER_IMAGE=[AR_path]
DOCKERFILE="src/main/docker/Dockerfile.jvm"
APP_NAME="coco-user-bg"

# 忽略 test 進行 build
./gradlew build -x test

# 建立 docker 並添加 tag
docker build -f src/main/docker/Dockerfile.jvm -t DOCKER_IMAGE .

# 將 docker image 推送到 Artifact Registry
docker push DOCKER_IMAGE

# 確保擁有正確的權限
#gcloud auth configure-docker \
#    asia-east1-docker.pkg.dev

# 將新版本部署到 k8s
# 新建立： kubectl apply -f k8s/test/eng-speaking-bg.yaml
gcloud config set project [projectId]
gcloud container clusters get-credentials [cluster_name] --region [region]
kubectl -n coco set image deployment APP_NAME APP_NAME=DOCKER_IMAGE
