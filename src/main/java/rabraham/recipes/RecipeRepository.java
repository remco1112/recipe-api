package rabraham.recipes;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.jpa.reactive.ReactorJpaSpecificationExecutor;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;

@MongoRepository
interface RecipeRepository extends ReactorCrudRepository<Recipe, String>, ReactorJpaSpecificationExecutor<Recipe> {
}
