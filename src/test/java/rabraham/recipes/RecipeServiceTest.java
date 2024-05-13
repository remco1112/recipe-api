package rabraham.recipes;

import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
public class RecipeServiceTest {
    private static final String RECIPE_ID = "recipe id";

    @Inject
    private RecipeRepository recipeRepository;

    @Inject
    private RecipeService recipeService;

    @Test
    void createRecipeSavesRecipe() {
        final Recipe recipe = mock(Recipe.class);
        final Mono<Recipe> expected = (Mono<Recipe>) mock(Mono.class);
        when(recipeRepository.save(recipe))
                .thenReturn(expected);

        final Mono<Recipe> actual = recipeService.createRecipe(recipe);

        assertSame(expected, actual);
    }

    @Test
    void createRecipeThrowsWhenRecipeHasId() {
        final Recipe recipe = mockRecipeWithId();

        final Mono<Recipe> actual = recipeService.createRecipe(recipe);

        StepVerifier.create(actual)
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(IllegalArgumentException.class, error);
                    assertEquals("Cannot create recipe with id", error.getMessage());
                }).verify();
    }

    @Test
    void deleteRecipeDeletesRecipeAndReturnsRecipeId() {
        when(recipeRepository.deleteById(RECIPE_ID))
                .thenReturn(Mono.just(1L));

        final Mono<String> actual = recipeService.deleteRecipe(RECIPE_ID);

        StepVerifier.create(actual)
                .expectNext(RECIPE_ID)
                .verifyComplete();
    }

    @Test
    void deleteRecipeThrowsWhenRecipeDoesNotExist() {
        when(recipeRepository.deleteById(RECIPE_ID))
                .thenReturn(Mono.just(0L));

        final Mono<String> actual = recipeService.deleteRecipe(RECIPE_ID);

        StepVerifier.create(actual)
                .expectErrorSatisfies(this::assertRecipeDoesNotExistError)
                .verify();
    }

    @Test
    void getRecipeReturnsRecipe() {
        final Recipe recipe = mock(Recipe.class);
        when(recipeRepository.findById(RECIPE_ID))
                .thenReturn(Mono.just(recipe));

        final Mono<Recipe> actual = recipeService.getRecipe(RECIPE_ID);

        StepVerifier.create(actual)
                .expectNext(recipe)
                .verifyComplete();
    }

    @Test
    void getRecipeThrowsWhenRecipeDoesNotExist() {
        when(recipeRepository.findById(RECIPE_ID))
                .thenReturn(Mono.empty());

        final Mono<Recipe> actual = recipeService.getRecipe(RECIPE_ID);

        StepVerifier.create(actual)
                .expectErrorSatisfies(this::assertRecipeDoesNotExistError)
                .verify();
    }

    @Test
    void updateRecipeUpdatesRecipe() {
        final Recipe recipeBeforeUpdate = mockRecipeWithId();
        final Recipe recipeAfterUpdate = mock(Recipe.class);
        when(recipeRepository.existsById(RECIPE_ID))
                .thenReturn(Mono.just(true));

        when(recipeRepository.update(recipeBeforeUpdate))
                .thenReturn(Mono.just(recipeAfterUpdate));

        final Mono<Recipe> actual = recipeService.updateRecipe(recipeBeforeUpdate);

        StepVerifier.create(actual)
                .expectNext(recipeAfterUpdate)
                .verifyComplete();
    }

    @Test
    void updateRecipeThrowsWhenRecipeNotFound() {
        final Recipe recipeBeforeUpdate = mockRecipeWithId();
        when(recipeRepository.existsById(RECIPE_ID))
                .thenReturn(Mono.just(false));

        final Mono<Recipe> actual = recipeService.updateRecipe(recipeBeforeUpdate);

        StepVerifier.create(actual)
                .expectErrorSatisfies(this::assertRecipeDoesNotExistError);
    }

    @Test
    void listRecipesListsRecipes() {
        final PredicateSpecification<Recipe> mockPredicateSpecification = (PredicateSpecification<Recipe>) mock(PredicateSpecification.class);
        final Flux<Recipe> expected = (Flux<Recipe>) mock(Flux.class);
        when(recipeRepository.findAll(mockPredicateSpecification))
                .thenReturn(expected);

        final RecipeCriteria mockCriteria = mock(RecipeCriteria.class);
        when(mockCriteria.matches()).thenReturn(mockPredicateSpecification);

        final Flux<Recipe> actual = recipeService.listRecipes(mockCriteria);

        assertSame(expected, actual);
    }

    private void assertRecipeDoesNotExistError(Throwable error) {
        assertInstanceOf(RecipeDoesNotExistException.class, error);
        assertEquals(RECIPE_ID, ((RecipeDoesNotExistException) error).getRecipeId());
    }

    @MockBean(RecipeRepository.class)
    RecipeRepository mockRepository() {
        return mock(RecipeRepository.class);
    }

    private static Recipe mockRecipeWithId() {
        final Recipe recipe = mock(Recipe.class);
        when(recipe.getId()).thenReturn(RECIPE_ID);
        return recipe;
    }
}
