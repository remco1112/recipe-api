package rabraham.recipes;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class RecipeServiceImpl implements RecipeService {

    @Inject
    private RecipeRepository recipeRepository;

    @Override
    public Mono<Recipe> createRecipe(Recipe recipe) {
        return recipe.getId() == null
                ? recipeRepository.save(recipe)
                : Mono.error(new IllegalArgumentException("Cannot create recipe with id"));
    }

    @Override
    public Mono<String> deleteRecipe(String recipeId) {
        return recipeRepository.deleteById(recipeId)
                .flatMap(deletedCount -> deletedCount == 0
                        ? Mono.error(new RecipeDoesNotExistException(recipeId))
                        : Mono.just(recipeId)
                );
    }

    @Override
    public Mono<Recipe> getRecipe(String recipeId) {
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeDoesNotExistException(recipeId)));
    }

    @Override
    @Transactional
    public Mono<Recipe> updateRecipe(Recipe recipe) {
        final String recipeId = recipe.getId();
        return recipeRepository.existsById(recipeId)
                .flatMap(exists -> exists
                        ? recipeRepository.update(recipe)
                        : Mono.error(new RecipeDoesNotExistException(recipeId)));
    }

    @Override
    public Flux<Recipe> listRecipes(RecipeCriteria criteria) {
        return recipeRepository.findAll(criteria.matches());
    }
}
