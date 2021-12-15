# Verteilte Systeme Labor
This project is developed in the course "verteilte systeme labor" of FH Karlsruhe.
The task is to partly migrate a legacy monolith application to new micro-services using Spring, docker and kubernetes.
TBD: integration of the remaining legacy application

## How to start the project
1. Clone this repository
2. Navigate to the k8s folder
3. in console (for example power shell): minikube start and afterwards kubectl apply -f microservices.yaml

## Install and use istio
1. Download istio (https://github.com/istio/istio/releases/tag/1.12.1)
2. minikube start
3. go into the downloded istio-folder (bin)
4. istioctl install
5. kubectl label namespace default istio-injection=enabled
6. navigate to k8s folder
7. kubectl apply -f microservices.yaml

## Get Visualizations with Kiali, Prometheus and Grafana
1. navigate to the istio-folder
2. kubectl apply -f samples/addons
3. Kiali: kubectl port-forward svc/kiali -n istio-system 20001
4. Prometheus: kubectl -n istio-system port-forward deployment/prometheus 9090:9090
5. Grafana: kubectl -n istio-system port-forward deployment/grafana 3000:3000
