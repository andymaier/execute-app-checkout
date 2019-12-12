#!/bin/bash
mvn clean install
docker build -t localhost:5000/msa-checkout-test:version1.0 .
docker push localhost:5000/msa-checkout-test:version1.0
k apply -f checkout-deployment.yaml
