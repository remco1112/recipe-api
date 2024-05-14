# Recipe API

## Running the application

Requires docker

1. Build docker image: `./mvnw clean package -Dpackaging=docker-native -Dmicronaut.aot.enabled=true -Pgraalvm`
2. Start services: `docker compose up`
3. Navigate to `localhost:8080/docs` to try the API