package rabraham.recipes;

import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
public class RecipeServiceTest {

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
        final Recipe recipe = mock(Recipe.class);
        when(recipe.getId()).thenReturn("recipe id");

        final Mono<Recipe> actual = recipeService.createRecipe(recipe);

        StepVerifier.create(actual)
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(IllegalArgumentException.class, error);
                    assertEquals("Cannot create recipe with id", error.getMessage());
                }).verify();
    }

    @MockBean(RecipeRepository.class)
    RecipeRepository mockRepository() {
        return mock(RecipeRepository.class);
    }
}
