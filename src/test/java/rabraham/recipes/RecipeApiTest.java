package rabraham.recipes;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rabraham.recipes.model.GetRecipeDTO;
import rabraham.recipes.model.IngredientDTO;
import rabraham.recipes.model.RecipeDTO;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class RecipeApiTest {
    private static final RecipeDTO RECIPE1 = new RecipeDTO(
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
    void createRecipesReturnsRecipe() {
        final HttpResponse<GetRecipeDTO> createResponse = recipeClient.create(RECIPE1);
        final Optional<GetRecipeDTO> responseBody = createResponse.getBody();

        assertEquals(HttpStatus.CREATED, createResponse.status());
        assertTrue(createResponse.getBody().isPresent());

        final GetRecipeDTO getRecipeDTO = responseBody.get();
        assertNotNull(getRecipeDTO.getId());
        assertEquals(RECIPE1, responseBody.get().getRecipe());
    }

    @Test
    void listRecipesReturnsCreatedRecipe() {
        final GetRecipeDTO createdRecipe = createRecipe(RECIPE1);

        final List<GetRecipeDTO> recipes = listAllRecipes();

        assertEquals(1, recipes.size());
        assertEquals(createdRecipe, recipes.get(0));
    }

    @AfterEach
    void clearDB() {
        recipeRepository.deleteAll().block();
    }

    private List<GetRecipeDTO> listAllRecipes() {
        return recipeClient.list(null, null, null, null, null);
    }

    private GetRecipeDTO createRecipe(RecipeDTO recipeDTO) {
        return recipeClient.create(recipeDTO)
                .getBody()
                .orElseThrow();
    }
}
