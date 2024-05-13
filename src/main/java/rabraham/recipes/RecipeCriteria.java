package rabraham.recipes;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

import java.util.List;

record RecipeCriteria(
        @Nullable Boolean vegetarian,
        @Nullable Integer servings,
        @Nullable String search,
        @Nullable List<String> includeIngredients,
        @Nullable List<String> excludeIngredients
) {
    private PredicateSpecification<Recipe> matchesVegetarian() {
        return (root, cb) -> cb.equal(root.get("vegetarian"), vegetarian);
    }

    private PredicateSpecification<Recipe> matchesServings() {
        return (root, cb) -> cb.equal(root.get("servings"), servings);
    }

    private PredicateSpecification<Recipe> containsSearchQueryInInstructions() {
        return (root, cb) -> cb.like(root.get("instructions"), search);
    }

    private PredicateSpecification<Recipe> includesIngredients() {
        return (root, cb) -> {
            // TODO Join broken: "Expected an association for attribute name: ingredients"
            final var in = cb.in(root.join("ingredients").get("name"));
            includeIngredients.forEach(in::value);
            return in;
        };
    }

    private PredicateSpecification<Recipe> excludesIngredients() {
        return (root, cb) -> {
            // TODO Join broken: "Expected an association for attribute name: ingredients"
            final var in = cb.in(root.join("ingredients").get("name"));
            includeIngredients.forEach(in::value);
            return in.not();
        };
    }

    PredicateSpecification<Recipe> matches() {
        PredicateSpecification<Recipe> predicate = (PredicateSpecification<Recipe>) PredicateSpecification.ALL;
        if (vegetarian != null) {
            predicate = predicate.and(matchesVegetarian());
        }
        if (servings != null) {
            predicate = predicate.and(matchesServings());
        }
        if (search != null) {
            predicate = predicate.and(containsSearchQueryInInstructions());
        }
        if (includeIngredients != null) {
            predicate = predicate.and(includesIngredients());
        }
        if (excludeIngredients != null) {
            predicate = predicate.and(excludesIngredients());
        }
        return predicate;
    }
}
