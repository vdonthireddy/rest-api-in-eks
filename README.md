# How to deploy a java rest api to EKS

### Login to your ecr
```
aws ecr get-login-password --region us-east-2| docker login --username AWS --password-stdin 759693498865.dkr.ecr.us-east-2.amazonaws.com
```

```
mvn clean install

docker buildx build --platform linux/amd64 -t hello-world-java:v12 .
docker tag hello-world-java:v12 759693498865.dkr.ecr.us-east-2.amazonaws.com/hello-repository:v12
docker push 759693498865.dkr.ecr.us-east-2.amazonaws.com/hello-repository:v12
```

### Login to ec2-instance
```
chmod 400 "K8s_Client_Server_Key.pem"
ssh -i "K8s_Client_Server_Key.pem" ec2-user@ec2-3-144-31-238.us-east-2.compute.amazonaws.com
```

### upload the deployment.yaml file to S3 and run the following after connecting to ec2-instance
```
kubectl delete -f https://redwoods-eks-resources.s3.us-east-2.amazonaws.com/deployment.yaml
kubectl apply -f https://redwoods-eks-resources.s3.us-east-2.amazonaws.com/deployment.yaml
```

## PORT FORWARD FROM THE POD TO EC2-INSTANCE:
### kubectl get pods 
### kubectl port-forward microservice-deployment-547b765cf6-wzdt7 8080:8080
### In another terminal tab, connect to ec2-instance again and run the following:
```
curl http://localhost:8080/hi
```

## SERVICE IP:
### NodePort can be retrieved from the following:
```
kubectl get svc
```
### look at your service and in the PORT(s) column, take the second port (e.g. 80:30445/TCP) and your NodePort is 30445
### get the IP address and do an "exec -it" into one of the containers by using the following:
```
kubectl exec -it microservice-deployment-b69dd7469-bpwnm -- /bin/bash
```
### and do a curl on the service ip (from the pod terminal)
```
curl http://10.100.222.79/hi OR curl http://10.100.222.79:80/hi note that port 80 is service port
```

## NODE IP:
### you can also do a curl on the node ip with nodeport port.
### get the node ip by using the following command (ec2-instance terminal):
```
kubectl get nodes -o wide
```
### get the ip from the node and use the following command (from pod termainl)
```
curl http://192.168.187.177:30445/hi (note that ip belongs to node and 30445 is from the NodePort)
```
