package rabraham.recipes;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import rabraham.recipes.model.GetRecipeDTO;
import rabraham.recipes.model.RecipeDTO;

import java.util.List;

@Client("/recipes")
interface RecipeClient {

    @Get
    HttpResponse<List<GetRecipeDTO>> list(
            @QueryValue @Nullable Boolean vegetarian,
            @QueryValue @Nullable Integer servings,
            @QueryValue @Nullable String search,
            @QueryValue @Nullable List<String> includeIngredient,
            @QueryValue @Nullable List<String> excludeIngredient
    );

    @Get("/{id}")
    HttpResponse<GetRecipeDTO> get(@PathVariable String id);

    @Put("/{id}")
    HttpResponse<GetRecipeDTO> update(@PathVariable String id, @Body RecipeDTO recipe);

    @Post
    HttpResponse<GetRecipeDTO> create(@Body RecipeDTO recipe);

    @Delete("/{id}")
    HttpResponse<Void> delete(@PathVariable String id);
}
