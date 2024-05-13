package rabraham.recipes;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import rabraham.recipes.api.RecipeApi;
import rabraham.recipes.model.GetRecipeDTO;
import rabraham.recipes.model.IngredientDTO;
import rabraham.recipes.model.RecipeDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RecipeController implements RecipeApi {

    @Inject
    private RecipeRepository recipeRepository;

    @Override
    public Mono<HttpResponse<@Valid GetRecipeDTO>> create(RecipeDTO recipeDTO) {
        return recipeRepository.save(toRecipe(recipeDTO))
                .map(this::toGetDto)
                .map(HttpResponse::created);
    }

    @Override
    public Mono<HttpResponse<Void>> delete(String recipeId) {
        return recipeRepository.deleteById(recipeId)
                .map(_ignore -> HttpResponse.noContent());
    }

    @Override
    public Mono<@Valid GetRecipeDTO> get(String recipeId) {
        return recipeRepository.findById(recipeId)
                .map(this::toGetDto);
    }

    @Override
    public Mono<@NotNull List<@Valid GetRecipeDTO>> list(Boolean vegetarian, Integer servings, String search, List<@NotNull String> includeIngredient, List<@NotNull String> excludeIngredient) {
        return recipeRepository.findAll() // TODO query params
                .map(this::toGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Mono<@Valid GetRecipeDTO> update(String recipeId, RecipeDTO recipeDTO) {
        final Recipe recipe = toRecipe(recipeDTO);
        recipe.setId(recipeId);
        return recipeRepository.update(recipe)
                .map(this::toGetDto);
    }

    private Recipe toRecipe(RecipeDTO recipeDTO) {
        final Recipe recipe = new Recipe();
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setInstructions(recipeDTO.getInstructions());
        recipe.setServings(recipeDTO.getServings());
        recipe.setVegetarian(recipeDTO.getVegetarian());
        recipe.setIngredients(recipeDTO.getIngredients().stream().map(this::toIngredient).toList());
        return recipe;
    }

    private Ingredient toIngredient(IngredientDTO ingredientDTO) {
        final Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDTO.getName());
        ingredient.setQuantity(ingredientDTO.getQuantity());
        return ingredient;
    }

    private GetRecipeDTO toGetDto(Recipe recipe) {
        return new GetRecipeDTO(
                recipe.getId(),
                toDto(recipe)
        );
    }

    private RecipeDTO toDto(Recipe recipe) {
        return new RecipeDTO(
                recipe.getTitle(),
                recipe.getInstructions(),
                recipe.getServings(),
                recipe.isVegetarian(),
                recipe.getIngredients().stream().map(this::toDto).toList()
        );
    }

    private IngredientDTO toDto(Ingredient ingredient) {
        return new IngredientDTO(ingredient.getName())
                .quantity(ingredient.getQuantity());
    }
}
