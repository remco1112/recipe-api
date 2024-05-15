package rabraham.recipes;

import io.micronaut.http.HttpResponse;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import rabraham.recipes.model.GetRecipeDTO;
import rabraham.recipes.model.IngredientDTO;
import rabraham.recipes.model.RecipeDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static rabraham.recipes.HttpAssertions.*;

@MicronautTest
class RecipeControllerTest {
    private static final String ID = "000000000000000000000000";
    private static final RecipeDTO RECIPE_DTO = createRecipeDto();
    private static final Recipe RECIPE = createRecipe();
    private static final Recipe RECIPE_WITH_ID = createRecipeWithId();
    private static final GetRecipeDTO EXPECTED_GET_RECIPE_DTO = new GetRecipeDTO(ID, RECIPE_DTO);
    private static final RecipeCriteria RECIPE_CRITERIA
            = new RecipeCriteria(true, 4, "search", List.of("ingredient1", "ingredient2"), List.of("ingredient2"));

    @Inject
    private RecipeService recipeService;

    @Inject
    private RecipeClient recipeClient;

    @Test
    void createCallsServiceAndReturnsCreatedResponse() {
        when(recipeService.createRecipe(RECIPE))
                .thenReturn(Mono.just(RECIPE_WITH_ID));

        final HttpResponse<GetRecipeDTO> response = recipeClient.create(RECIPE_DTO);

        assertCreatedWithBody(EXPECTED_GET_RECIPE_DTO, response);
    }

    @Test
    void deleteCallsServiceAndReturnsNoContent() {
        when(recipeService.deleteRecipe(ID))
                .thenReturn(Mono.just(ID));

        final HttpResponse<Void> response = recipeClient.delete(ID);

        assertNoContentResponse(response);
    }

    @Test
    void deleteReturnsNotFoundResponseWhenRecipeDoesNotExist() {
        when(recipeService.deleteRecipe(ID))
                .thenReturn(createRecipeDoesNotExistMono());

        final HttpResponse<Void> response = recipeClient.delete(ID);

        assertNotFoundResponse(response);
    }

    @Test
    void getCallsServiceAndReturnsOk() {
        when(recipeService.getRecipe(ID))
                .thenReturn(Mono.just(RECIPE_WITH_ID));

        final HttpResponse<GetRecipeDTO> response = recipeClient.get(ID);

        assertOkWithBody(EXPECTED_GET_RECIPE_DTO, response);
    }

    @Test
    void getReturnsNotFoundResponseWhenRecipeDoesNotExist() {
        when(recipeService.getRecipe(ID))
                .thenReturn(createRecipeDoesNotExistMono());
    }

    @Test
    void listCallsServiceAndReturnsOk() {
        when(recipeService.listRecipes(RECIPE_CRITERIA))
                .thenReturn(Flux.just(RECIPE_WITH_ID, RECIPE_WITH_ID, RECIPE_WITH_ID));

        final HttpResponse<List<GetRecipeDTO>> response = recipeClient.list(
                RECIPE_CRITERIA.vegetarian(),
                RECIPE_CRITERIA.servings(),
                RECIPE_CRITERIA.search(),
                RECIPE_CRITERIA.includeIngredients(),
                RECIPE_CRITERIA.excludeIngredients()
        );

        assertOkWithBody(
                List.of(EXPECTED_GET_RECIPE_DTO, EXPECTED_GET_RECIPE_DTO, EXPECTED_GET_RECIPE_DTO),
                response
        );
    }

    @Test
    void updateCallsServiceAndReturnsOk() {
        when(recipeService.updateRecipe(RECIPE_WITH_ID))
                .thenReturn(Mono.just(RECIPE_WITH_ID));

        final HttpResponse<GetRecipeDTO> response = recipeClient.update(ID, RECIPE_DTO);

        assertOkWithBody(EXPECTED_GET_RECIPE_DTO, response);
    }

    @Test
    void updateReturnsNotFoundResponseWhenRecipeDoesNotExist() {
        when(recipeService.updateRecipe(RECIPE_WITH_ID))
                .thenReturn(createRecipeDoesNotExistMono());

        final HttpResponse<GetRecipeDTO> response = recipeClient.update(ID, RECIPE_DTO);

        assertNotFoundResponse(response);
    }

    private static Recipe createRecipe() {
        final Recipe recipe = new Recipe();

        recipe.setTitle("title");
        recipe.setInstructions("instructions");
        recipe.setServings(4);
        recipe.setVegetarian(true);

        final Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("name1");
        ingredient1.setQuantity("quantity1");

        final Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("name2");

        recipe.setIngredients(List.of(ingredient1, ingredient2));

        return recipe;
    }

    private static RecipeDTO createRecipeDto() {
        return new RecipeDTO(
                "title",
                "instructions",
                4,
                true,
                List.of(
                        new IngredientDTO("name1").quantity("quantity1"),
                        new IngredientDTO("name2")
                )
        );
    }

    private static Recipe createRecipeWithId() {
        final Recipe recipe = createRecipe();
        recipe.setId(ID);
        return recipe;
    }

    private static <E> Mono<E> createRecipeDoesNotExistMono() {
        return Mono.error(new RecipeDoesNotExistException(ID));
    }

    @MockBean(RecipeServiceImpl.class)
    RecipeService mockService() {
        return mock(RecipeServiceImpl.class);
    }
}
