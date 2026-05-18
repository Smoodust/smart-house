# Smart House Helm chart

## What is included

- backend Deployment with 2 replicas, readiness and liveness probes
- bridge Deployment and Service
- mosquitto Deployment and Service
- kafka-ui Deployment and Service
- prometheus Deployment and Service
- grafana Deployment and Service
- ConfigMap and Secret for shared configuration
- Liquibase Job as a Helm hook
- test-hub Job that is disabled by default
- PostgreSQL and Kafka as Helm dependencies

## Install

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm dependency update ./smart-house
helm upgrade --install smart-house ./smart-house -n smart-house --create-namespace
```

## Run test-hub only when needed

```bash
helm upgrade --install smart-house ./smart-house -n smart-house --create-namespace --set testHub.enabled=true
```

## Check the result

```bash
kubectl get pods,svc,job -n smart-house
```
