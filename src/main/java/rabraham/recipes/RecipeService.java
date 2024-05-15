package rabraham.recipes;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Mono<Recipe> createRecipe(Recipe recipe);

    Mono<String> deleteRecipe(String recipeId);

    Mono<Recipe> getRecipe(String recipeId);

    Mono<Recipe> updateRecipe(Recipe recipe);

    Flux<Recipe> listRecipes(RecipeCriteria criteria);
}
