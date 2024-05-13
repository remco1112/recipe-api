package rabraham.recipes;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Ingredient {

    private String name;
    private String quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
