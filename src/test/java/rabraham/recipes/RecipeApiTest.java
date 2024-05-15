package rabraham.recipes;

import io.micronaut.http.HttpResponse;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rabraham.recipes.model.GetRecipeDTO;
import rabraham.recipes.model.IngredientDTO;
import rabraham.recipes.model.RecipeDTO;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static rabraham.recipes.HttpAssertions.*;

@MicronautTest(transactional = false)
public class RecipeApiTest {
    private static final String NOT_FOUND_RECIPE_ID = "000000000000000000000000";
    private static final RecipeDTO RECIPE = new RecipeDTO(
            "cake",
            "mix, bake, eat",
            2,
            false,
            List.of(
                    new IngredientDTO("milk")
                            .quantity("1L"),
                    new IngredientDTO("butter"))
    );

    @Inject
    RecipeClient recipeClient;

    @Inject
    RecipeRepository recipeRepository;

    @Test
    void listRecipesReturnsEmptyListWhenDatabaseEmpty() {
        final List<GetRecipeDTO> recipes = listAllRecipes();

        assertTrue(recipes.isEmpty());
    }

    @Test
    void listRecipesReturnsCreatedRecipe() {
        final GetRecipeDTO createdRecipe = createRecipe();

        final List<GetRecipeDTO> recipes = listAllRecipes();

        assertEquals(1, recipes.size());
        assertEquals(createdRecipe, recipes.get(0));
    }

    @Test
    void deleteRecipeReturnsNotFoundWhenRecipeDoesNotExist() {
        final HttpResponse<Void> response = recipeClient.delete(NOT_FOUND_RECIPE_ID);

        assertNotFoundResponse(response);
    }

    @Test
    void deleteRecipeDeletesCreatedRecipe() {
        final GetRecipeDTO createdRecipe = createRecipe();

        final HttpResponse<Void> response = recipeClient.delete(createdRecipe.getId());

        assertNoContentResponse(response);

        final List<GetRecipeDTO> recipes = listAllRecipes();
        assertTrue(recipes.isEmpty());
    }

    @Test
    void getRecipeReturnsNotFoundWhenRecipeDoesNotExist() {
        final HttpResponse<GetRecipeDTO> response = recipeClient.get(NOT_FOUND_RECIPE_ID);

        assertNotFoundResponse(response);
    }

    @Test
    void getRecipeReturnsCreatedRecipe() {
        final GetRecipeDTO createdRecipe = createRecipe();

        final HttpResponse<GetRecipeDTO> response = recipeClient.get(createdRecipe.getId());

        assertOkWithBody(createdRecipe, response);
    }

    @Test
    void createRecipesReturnsRecipe() {
        final HttpResponse<GetRecipeDTO> response = recipeClient.create(RECIPE);

        assertCreatedWithBody(
                (Consumer<GetRecipeDTO>) (actualBody) -> {
                    assertNotNull(actualBody.getId());
                    assertEquals(RECIPE, actualBody.getRecipe());
                },
                response
        );
    }

    @Test
    void updateRecipeReturnsNotFoundWhenRecipeDoesNotExist() {
        final HttpResponse<GetRecipeDTO> response = recipeClient.update(NOT_FOUND_RECIPE_ID, RECIPE);

        assertNotFoundResponse(response);
    }

    @Test
    void updateRecipeUpdatesRecipe() {
        final GetRecipeDTO recipe = createRecipe();
        final RecipeDTO updatedRecipe = recipe.getRecipe()
                .title("new title");

        final HttpResponse<GetRecipeDTO> response = recipeClient.update(recipe.getId(), updatedRecipe);

        assertOkWithBody(
                (Consumer<GetRecipeDTO>) (actualBody) -> {
                    assertEquals(recipe.getId(), actualBody.getId());
                    assertEquals(recipe.getRecipe(), updatedRecipe);
                },
                response
        );

        final List<GetRecipeDTO> recipes = listAllRecipes();
        assertEquals(1, recipes.size());
        assertEquals(response.body(), recipes.get(0));
    }

    @AfterEach
    void clearDB() {
        recipeRepository.deleteAll().block();
    }

    private List<GetRecipeDTO> listAllRecipes() {
        return recipeClient.list(null, null, null, null, null)
                .getBody()
                .orElseThrow();
    }

    private GetRecipeDTO createRecipe() {
        return recipeClient.create(RecipeApiTest.RECIPE)
                .getBody()
                .orElseThrow();
    }
}
