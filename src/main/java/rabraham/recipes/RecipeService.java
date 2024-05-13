package rabraham.recipes;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class RecipeService {

    @Inject
    private RecipeRepository recipeRepository;

    public Mono<Recipe> createRecipe(Recipe recipe) {
        return recipe.getId() == null
                ? recipeRepository.save(recipe)
                : Mono.error(new IllegalArgumentException("Cannot create recipe with id"));
    }

    public Mono<String> deleteRecipe(String recipeId) {
        return recipeRepository.deleteById(recipeId)
                .flatMap(deletedCount -> deletedCount == 0
                        ? Mono.error(new RecipeDoesNotExistException(recipeId))
                        : Mono.just(recipeId)
                );
    }

    public Mono<Recipe> getRecipe(String recipeId) {
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeDoesNotExistException(recipeId)));
    }

    public Mono<Recipe> updateRecipe(Recipe recipe) {
        return recipeRepository.update(recipe);
    }

    public Flux<Recipe> listRecipes() {
        return recipeRepository.findAll();
    }
}
