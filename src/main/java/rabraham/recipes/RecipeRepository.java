package rabraham.recipes;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.jpa.JpaSpecificationExecutor;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.inject.Singleton;

@MongoRepository
interface RecipeRepository extends ReactorCrudRepository<Recipe, String>, JpaSpecificationExecutor<Recipe> {
}
