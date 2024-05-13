package rabraham.recipes;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rabraham.recipes.model.GetRecipeDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(transactional = false)
public class RecipeApiTest {

    @Inject
    RecipeClient recipeClient;

    @Test
    void getRecipesReturnsEmptyListWhenDatabaseEmpty() {
        final List<GetRecipeDTO> recipes = recipeClient.list(null, null, null, null, null);

        assertTrue(recipes.isEmpty());
    }
}
