#!/bin/bash

##################
### Apps Setup ###
##################

### Set parameters
program="ugur"
locationLong="westeurope"
locationShort="euw"
project="tracing"
stageLong="dev"
stageShort="d"
instance="001"

### Set variables

# AKS
aksName="aks$program$locationShort$project$stageShort$instance"

# Zookeeper
declare -A zookeeper
zookeeper["name"]="zookeeper"
zookeeper["namespace"]="kafka"
zookeeper["port"]=2181

# Kafka
declare -A kafka
kafka["name"]="kafka"
kafka["namespace"]="kafka"
kafka["port"]=9092
kafka["topic"]="tracing"

# Zipkin Server
declare -A zipkinserver
zipkinserver["name"]="zipkinserver"
zipkinserver["namespace"]="zipkin"
zipkinserver["port"]=9411

# Otel Collector
declare -A otelcollector
otelcollector["name"]="otelcollector"
otelcollector["namespace"]="otel"
otelcollector["grpcPort"]=4317
otelcollector["httpPort"]=4318
otelcollector["grpcEndpoint"]="http://${otelcollector[name]}.${otelcollector[namespace]}.svc.cluster.local:${otelcollector[grpcPort]}"

# Zipkin Exporter
declare -A zipkinexporter
zipkinexporter["name"]="zipkinexporter"
zipkinexporter["namespace"]="zipkin"
zipkinexporter["port"]=8080

# Proxy
declare -A proxy
proxy["name"]="proxy"
proxy["namespace"]="proxy"
proxy["port"]=8080

# First
declare -A first
first["name"]="first"
first["namespace"]="first"
first["port"]=8080

# Second
declare -A second
second["name"]="second"
second["namespace"]="second"
second["port"]=8080

# Third
declare -A third
third["name"]="third"
third["namespace"]="third"
third["port"]=8080

# Fourth
declare -A fourth
fourth["name"]="fourth"
fourth["namespace"]="fourth"
fourth["port"]=8080
fourth["nginxName"]="nginx-fourth"
fourth["nginxPort"]=80

# Fifth
declare -A fifth
fifth["name"]="fifth"
fifth["namespace"]="fifth"
fifth["port"]=8080

# Sixth
declare -A sixth
sixth["name"]="sixth"
sixth["namespace"]="sixth"
sixth["port"]=8080
#########

####################
### Build & Push ###
####################

# # To build for amd64
# --platform linux/amd64 \

# Zookeeper
docker build \
  --tag "${DOCKERHUB_NAME}/${zookeeper[name]}" \
  "../../apps/kafka/zookeeper/."
docker push "${DOCKERHUB_NAME}/${zookeeper[name]}"

# Kafka
docker build \
  --tag "${DOCKERHUB_NAME}/${kafka[name]}" \
  "../../apps/kafka/kafka/."
docker push "${DOCKERHUB_NAME}/${kafka[name]}"

# Zipkin Exporter
docker build \
  --tag "${DOCKERHUB_NAME}/${zipkinexporter[name]}" \
  "../../apps/${zipkinexporter[name]}/."
docker push "${DOCKERHUB_NAME}/${zipkinexporter[name]}"

# Proxy
docker build \
  --build-arg newRelicAppName=${proxy[name]} \
  --build-arg newRelicLicenseKey=$NEWRELIC_LICENSE_KEY \
  --tag "${DOCKERHUB_NAME}/${proxy[name]}" \
  "../../apps/${proxy[name]}/."
docker push "${DOCKERHUB_NAME}/${proxy[name]}"

# First
docker build \
  --tag "${DOCKERHUB_NAME}/${first[name]}" \
  "../../apps/${first[name]}/."
docker push "${DOCKERHUB_NAME}/${first[name]}"

# Second
docker build \
  --build-arg newRelicAppName=${second[name]} \
  --build-arg newRelicLicenseKey=$NEWRELIC_LICENSE_KEY \
  --tag "${DOCKERHUB_NAME}/${second[name]}" \
  "../../apps/${second[name]}/."
docker push "${DOCKERHUB_NAME}/${second[name]}"

# Third
docker build \
  --tag "${DOCKERHUB_NAME}/${third[name]}" \
  "../../apps/${third[name]}/."
docker push "${DOCKERHUB_NAME}/${third[name]}"

# Fourth (nginx)
docker build \
  --build-arg nginxName=${fourth[nginxName]} \
  --build-arg nginxPort=${fourth[nginxPort]} \
  --build-arg proxyPort=${fourth[port]} \
  --tag "${DOCKERHUB_NAME}/${fourth[nginxName]}" \
  "../../apps/nginx/."
docker push "${DOCKERHUB_NAME}/${fourth[nginxName]}"

# Fourth (app)
docker build \
  --build-arg newRelicAppName=${fourth[name]} \
  --build-arg newRelicLicenseKey=$NEWRELIC_LICENSE_KEY \
  --tag "${DOCKERHUB_NAME}/${fourth[name]}" \
  "../../apps/${fourth[name]}/."
docker push "${DOCKERHUB_NAME}/${fourth[name]}"

# Fifth
docker build \
  --build-arg newRelicAppName=${fifth[name]} \
  --build-arg newRelicLicenseKey=$NEWRELIC_LICENSE_KEY \
  --tag "${DOCKERHUB_NAME}/${fifth[name]}" \
  "../../apps/${fifth[name]}/."
docker push "${DOCKERHUB_NAME}/${fifth[name]}"

# Sixth
docker build \
  --build-arg otelExporterOtlpEndpoint=${otelcollector[grpcEndpoint]} \
  --tag "${DOCKERHUB_NAME}/${sixth[name]}" \
  "../../apps/${sixth[name]}/."
docker push "${DOCKERHUB_NAME}/${sixth[name]}"
#######

# ################
# ### Newrelic ###
# ################
# echo "Deploying Newrelic ..."

# kubectl apply -f https://download.newrelic.com/install/kubernetes/pixie/latest/px.dev_viziers.yaml && \
# kubectl apply -f https://download.newrelic.com/install/kubernetes/pixie/latest/olm_crd.yaml && \
# helm repo add newrelic https://helm-charts.newrelic.com && helm repo update && \
# kubectl create namespace newrelic ; helm upgrade --install newrelic-bundle newrelic/nri-bundle \
#   --wait \
#   --debug \
#   --set global.licenseKey=$NEWRELIC_LICENSE_KEY \
#   --set global.cluster=$aksName \
#   --namespace=newrelic \
#   --set newrelic-infrastructure.privileged=true \
#   --set global.lowDataMode=true \
#   --set ksm.enabled=true \
#   --set kubeEvents.enabled=true \
#   --set prometheus.enabled=true \
#   --set logging.enabled=true \
#   --set newrelic-pixie.enabled=true \
#   --set newrelic-pixie.apiKey=$PIXIE_API_KEY \
#   --set pixie-chart.enabled=true \
#   --set pixie-chart.deployKey=$PIXIE_DEPLOY_KEY \
#   --set pixie-chart.clusterName=$aksName
# #########

##########################
### Ingress Controller ###
##########################
echo "Deploying Ingress Controller ..."

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx && \
helm repo update; \
helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx \
  --namespace nginx --create-namespace \
  --wait \
  --debug \
  --set controller.replicaCount=1 \
  --set controller.nodeSelector."kubernetes\.io/os"="linux" \
  --set controller.image.image="ingress-nginx/controller" \
  --set controller.image.tag="v1.1.1" \
  --set controller.image.digest="" \
  --set controller.service.externalTrafficPolicy=Local \
  --set controller.admissionWebhooks.patch.nodeSelector."kubernetes\.io/os"="linux" \
  --set controller.admissionWebhooks.patch.image.image="ingress-nginx/kube-webhook-certgen" \
  --set controller.admissionWebhooks.patch.image.tag="v1.1.1" \
  --set controller.admissionWebhooks.patch.image.digest="" \
  --set defaultBackend.nodeSelector."kubernetes\.io/os"="linux" \
  --set defaultBackend.image.image="defaultbackend-amd64" \
  --set defaultBackend.image.tag="1.5" \
  --set defaultBackend.image.digest=""
#########

#############
### Kafka ###
#############

# Zookeeper
echo "Deploying Zookeeper ..."

helm upgrade ${zookeeper[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${zookeeper[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${zookeeper[name]} \
  --set namespace=${zookeeper[namespace]} \
  --set port=${zookeeper[port]} \
  ../charts/zookeeper

# Kafka
echo "Deploying Kafka ..."

helm upgrade ${kafka[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${kafka[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${kafka[name]} \
  --set namespace=${kafka[namespace]} \
  --set port=${kafka[port]} \
  ../charts/kafka

# Topic
echo "Checking topic [${kafka[topic]}] ..."

topicExists=$(kubectl exec -n "${kafka[namespace]}" "${kafka[name]}-0" -it -- bash \
  /kafka/bin/kafka-topics.sh \
  --bootstrap-server "${kafka[name]}.${kafka[namespace]}.svc.cluster.local:${kafka[port]}" \
  --list \
  | grep ${kafka[topic]})

if [[ $topicExists == "" ]]; then

  echo " -> Topic does not exist. Creating ..."
  while :
  do
    isTopicCreated=$(kubectl exec -n "${kafka[namespace]}" "${kafka[name]}-0" -it -- bash \
      /kafka/bin/kafka-topics.sh \
      --bootstrap-server "${kafka[name]}.${kafka[namespace]}.svc.cluster.local:${kafka[port]}" \
      --create \
      --topic ${kafka[topic]} \
      2> /dev/null)

    if [[ $isTopicCreated == "" ]]; then
      echo " -> Kafka pods are not fully ready yet. Waiting ..."
      sleep 2
      continue
    fi

    echo -e " -> Topic is created successfully.\n"
    break

  done
else
  echo -e " -> Topic already exists.\n"
fi
#########

##############
### Zipkin ###
##############

# Zipkin Server
echo "Deploying Zipkin server..."

helm upgrade ${zipkinserver[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${zipkinserver[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${zipkinserver[name]} \
  --set namespace=${zipkinserver[namespace]} \
  --set port=${zipkinserver[port]} \
  "../charts/${zipkinserver[name]}"

# Zipkin Exporter
echo "Deploying Zipkin exporter..."

helm upgrade ${zipkinexporter[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${zipkinexporter[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set newRelicLicenseKey=$NEWRELIC_LICENSE_KEY \
  --set name=${zipkinexporter[name]} \
  --set namespace=${zipkinexporter[namespace]} \
  --set port=${zipkinexporter[port]} \
  "../charts/${zipkinexporter[name]}"
#########

######################
### Otel Collector ###
######################
echo "Deploying Otel collector..."

helm upgrade ${otelcollector[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${otelcollector[namespace]} \
  --set newRelicLicenseKey=$NEWRELIC_LICENSE_KEY \
  --set name=${otelcollector[name]} \
  --set namespace=${otelcollector[namespace]} \
  --set grpcPort=${otelcollector[grpcPort]} \
  --set httpPort=${otelcollector[httpPort]} \
  --set fluentPort=${otelcollector[fluentPort]} \
  --set newRelicOtlpGrpcEndpoint=$newRelicOtlpGrpcEndpoint \
  "../charts/${otelcollector[name]}"
#########

#############
### Proxy ###
#############
echo "Deploying proxy..."

helm upgrade ${proxy[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${proxy[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${proxy[name]} \
  --set namespace=${proxy[namespace]} \
  --set port=${proxy[port]} \
  "../charts/${proxy[name]}"
#########

#################
### First app ###
#################
echo "Deploying first app..."

helm upgrade ${first[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${first[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${first[name]} \
  --set namespace=${first[namespace]} \
  --set port=${first[port]} \
  "../charts/${first[name]}"
#########

##################
### Second app ###
##################
echo "Deploying second app..."

helm upgrade ${second[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${second[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${second[name]} \
  --set namespace=${second[namespace]} \
  --set port=${second[port]} \
  "../charts/${second[name]}"
#########

#################
### Third App ###
#################
echo "Deploying third app..."

helm upgrade ${third[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${third[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${third[name]} \
  --set namespace=${third[namespace]} \
  --set port=${third[port]} \
  "../charts/${third[name]}"
#########

##################
### Fourth App ###
##################
echo "Deploying fourth app..."

helm upgrade ${fourth[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${fourth[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${fourth[name]} \
  --set namespace=${fourth[namespace]} \
  --set port=${fourth[port]} \
  --set nginxName=${fourth[nginxName]} \
  --set nginxPort=${fourth[nginxPort]} \
  "../charts/${fourth[name]}"
#########

##################
### Fifth App ###
##################
echo "Deploying fifth app..."

helm upgrade ${fifth[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${fifth[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${fifth[name]} \
  --set namespace=${fifth[namespace]} \
  --set port=${fifth[port]} \
  "../charts/${fifth[name]}"
#########

#################
### Sixth App ###
#################
echo "Deploying sixth app..."

helm upgrade ${sixth[name]} \
  --install \
  --wait \
  --debug \
  --create-namespace \
  --namespace ${sixth[namespace]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set name=${sixth[name]} \
  --set namespace=${sixth[namespace]} \
  --set port=${sixth[port]} \
  "../charts/${sixth[name]}"
#########
