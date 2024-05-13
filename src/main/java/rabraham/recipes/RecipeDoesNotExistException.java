package rabraham.recipes;

public class RecipeDoesNotExistException extends Exception {
    private final String recipeId;

    public RecipeDoesNotExistException(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeId() {
        return recipeId;
    }
}
