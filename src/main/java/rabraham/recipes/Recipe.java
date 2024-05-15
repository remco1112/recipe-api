package rabraham.recipes;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import lombok.Data;

import java.util.List;

@Data
@MappedEntity
public class Recipe {
    @Id
    @GeneratedValue(GeneratedValue.Type.UUID)
    private String id;

    private boolean vegetarian;
    private String title;
    private String instructions;
    private int servings;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;
}
