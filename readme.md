By uruchomić projekt:
```shell
mvn com.google.cloud.tools:jib-maven-plugin:3.1.1:dockerBuild
cd docker
docker-compose -f postgres.yml up -d postgres-service
docker run -p 8080:8080 --name recycoin-backend --env SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/postgres recycoin-backend
```