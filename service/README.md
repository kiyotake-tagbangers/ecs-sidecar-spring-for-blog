# Run Locally

## Spring Boot app

```shell
export JAVA_HOME=`/usr/libexec/java_home -v 11`

$ java -version
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode)
```

### default profile

```
./mvnw spring-boot:run
```

or

```
./mvnw clean package

java -jar $(\ls target/*jar)
```

```
$ curl http://localhost:8081/actuator/health
{"status":"UP"}

$ curl localhost:8081/
{"profile":"default","message":"hello, default"}

$ curl localhost:8081/index
{"profile":"default","message":"hello, default"}
```

### development profile

```
export SPRING_PROFILES_ACTIVE=development

java -jar $(\ls target/*jar)
```

or

```
unset SPRING_PROFILES_ACTIVE

java -jar -Dspring.profiles.active=development $(\ls target/*jar)
```

or

```
unset SPRING_PROFILES_ACTIVE

./mvnw spring-boot:run -Dspring-boot.run.profiles=development
```

```
$ curl localhost:8081/
{"message":"hello, development"}
```

## Build Image

### spring

```shell
./mvnw spring-boot:build-image
```

### [Cloud Native Buildpacks](https://buildpacks.io/docs/tools/pack/)

```shell
./mvnw clean package

ARTIFACT_ID=$(./mvnw org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.artifactId -q -DforceStdout)

ARTIFACT_VERSION=$(./mvnw org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout)

pack build $ARTIFACT_ID:$ARTIFACT_VERSION -p target/$ARTIFACT_ID-$ARTIFACT_VERSION.jar --builder cloudfoundry/cnb:bionic
```

### run as the container

```
ARTIFACT_ID=$(./mvnw org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.artifactId -q -DforceStdout)

ARTIFACT_VERSION=$(./mvnw org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout)

$ docker image ls | grep $ARTIFACT_ID
demo-spring-app                                                                                 0.0.1                                            3c366111419b   41 years ago    259MB

docker run --name $ARTIFACT_ID -d -p 8081:8081 $ARTIFACT_ID:$ARTIFACT_VERSION

$ docker container ls | grep $ARTIFACT_ID
f47fbb8d00c6   demo-spring-app:0.0.1   "/cnb/process/web"   24 seconds ago   Up 22 seconds   0.0.0.0:8081->8081/tcp   demo-spring-app
```

- development profile

```
docker run --name $ARTIFACT_ID -d -p 8081:8081 -e "SPRING_PROFILES_ACTIVE=development" $ARTIFACT_ID:$ARTIFACT_VERSION
```

---
## Running side-car container pattern

- running nginx in front of spring app

```shell
export ARTIFACT_ID=$(./mvnw org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.artifactId -q -DforceStdout)

export ARTIFACT_VERSION=$(./mvnw org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout)

docker-compose build

docker-compose up -d

$ docker-compose ps
       Name                      Command               State           Ports
-------------------------------------------------------------------------------------
demo-spring-app       /cnb/process/web                 Up      0.0.0.0:8081->8081/tcp
reverse-proxy-nginx   /docker-entrypoint.sh ngin ...   Up      0.0.0.0:80->80/tcp

$ curl -s localhost | grep "<h1>"
<h1>nginx index page</h1>

$ curl http://localhost/spring
{"message":"hello, default"}

$ curl http://localhost/spring/actuator/health
{"status":"UP"}
```

