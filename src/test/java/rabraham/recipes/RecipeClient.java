package rabraham.recipes;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import rabraham.recipes.model.GetRecipeDTO;
import rabraham.recipes.model.RecipeDTO;

import java.util.List;

@Client("/recipes")
interface RecipeClient {

    @Get
    List<GetRecipeDTO> list(
            @QueryValue @Nullable Boolean vegetarian,
            @QueryValue @Nullable Integer servings,
            @QueryValue @Nullable String search,
            @QueryValue @Nullable List<String> includeIngredients,
            @QueryValue @Nullable List<String> excludeIngredients
    );

    @Get("/{id}")
    GetRecipeDTO get(@NotNull @PathVariable String id);

    @Put("/{id}")
    GetRecipeDTO update(@NotNull String id, @NotNull @Valid RecipeDTO recipe);

    @Post
    HttpResponse<GetRecipeDTO> create(@NotNull @Valid RecipeDTO recipe);

    @Delete("/{id}")
    HttpResponse<Void> delete(@NotNull @PathVariable String id);
}
