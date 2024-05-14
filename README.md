# Recipe API

Cloud-native reactive Recipe API using Micronaut and MongoDB. Designed for performance and usage in a service
architecture.

## Key design choices

- Uses Micronaut with AOT compilation for high efficiency, quick boot-ups, and easy of integration testing with
  testcontainers.
- Uses Project Reactor for efficient concurrency during I/O (database) tasks
- Uses a design-first approach where server classes are generated from the OpenAPI specification
- Uses a document database (MongDB) since it is best suited for this type of data. Recipes are essentially documents,
  there are no relations, and the exact fields will likely change over time.

## Known limitations

- Due to issues with the criteria builder used for the list query it is not possible to filter by included and excluded
  ingredients. See disabled test `X` the scenario.
- No authentication or authorisation is implemented. It is advisable to authenticate users and only allow users to
  change recipes they created themselves. Authentication could be implemented through an external authentication
  provider with JWT.

## Testing

Simply run `./mvnw clean test` to run all unit and integration tests. Integration tests automatically start an
integrated webserver and a MongoDB database via testcontainers.

## Running

Requires docker

1. Build docker image: `./mvnw clean package -Dpackaging=docker-native -Dmicronaut.aot.enabled=true -Pgraalvm`
2. Start services: `docker compose up`
3. Navigate to `localhost:8080/docs` to try the API via Swagger UI