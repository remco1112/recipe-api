package rabraham.recipes;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class RecipeApiValidationTest {

    private static final String VALID_BODY =
            """             
                    {
                        "title": "title",
                        "instructions": "instructions",
                        "servings": 1,
                        "vegetarian": true,
                        "ingredients": [
                            {
                                "name": "ingredient1",
                                "quantity": "quantity1"
                            },
                            {
                                "name": "ingredient2"
                            }
                        ]
                    }
                    """;

    private static final String VALID_ID = "000000000000000000000000";

    @Inject
    @Client("/recipes")
    HttpClient client;

    @ParameterizedTest
    @MethodSource("invalidRequestBodies")
    void createRecipeReturnsBadRequest(String body) {
        HttpRequest<?> request = HttpRequest.POST("/", body)
                .contentType(MediaType.APPLICATION_JSON_TYPE);

        var responseException = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().exchange(request));

        assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
    }

    @ParameterizedTest
    @MethodSource("invalidRequestBodies")
    void putRecipeReturnsBadRequestForInvalidRequestBodies(String body) {
        HttpRequest<?> request = HttpRequest.PUT("/" + VALID_ID, body)
                .contentType(MediaType.APPLICATION_JSON_TYPE);

        var responseException = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().exchange(request));

        assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
    }

    @ParameterizedTest
    @MethodSource("invalidIds")
    void putRecipeReturnsBadRequestForInvalidId(String id) {
        HttpRequest<?> request = HttpRequest.PUT("/" + id, VALID_BODY)
                .contentType(MediaType.APPLICATION_JSON_TYPE);

        var responseException = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().exchange(request));

        assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
    }

    static List<String> invalidRequestBodies() {
        return List.of(
                "",
                "{}",
                """
                        {
                            "instructions": "instructions",
                            "servings": 1,
                            "vegetarian": true,
                            "ingredients": [
                                {
                                    "name": "ingredient1",
                                    "quantity": "quantity1"
                                },
                                {
                                    "name": "ingredient2"
                                }
                            ]
                        }
                        """,
                """
                        {
                            "title": "title",
                            "servings": 1,
                            "vegetarian": true,
                            "ingredients": [
                                {
                                    "name": "ingredient1",
                                    "quantity": "quantity1"
                                },
                                {
                                    "name": "ingredient2"
                                }
                            ]
                        }
                        """,
                """
                        {
                            "title": "title",
                            "instructions": "instructions",
                            "vegetarian": true,
                            "ingredients": [
                                {
                                    "name": "ingredient1",
                                    "quantity": "quantity1"
                                },
                                {
                                    "name": "ingredient2"
                                }
                            ]
                        }
                        """,
                """
                        {
                            "title": "title",
                            "instructions": "instructions",
                            "servings": 1,
                            "ingredients": [
                                {
                                    "name": "ingredient1",
                                    "quantity": "quantity1"
                                },
                                {
                                    "name": "ingredient2"
                                }
                            ]
                        }
                        """,
                """
                        {
                            "title": "title",
                            "instructions": "instructions",
                            "servings": 1,
                            "vegetarian": true
                        }
                        """,
                """
                        {
                            "title": "title",
                            "instructions": "instructions",
                            "servings": 0,
                            "vegetarian": true,
                            "ingredients": [
                                {
                                    "name": "ingredient1",
                                    "quantity": "quantity1"
                                },
                                {
                                    "name": "ingredient2"
                                }
                            ]
                        }
                        """,
                """
                        {
                            "title": "title",
                            "instructions": "instructions",
                            "servings": 1,
                            "vegetarian": "true",
                        }
                        """,
                """
                        {
                            "title": "title",
                            "instructions": "instructions",
                            "servings": 1,
                            "vegetarian": true,
                            "ingredients": []
                        }
                        """
        );
    }

    static List<String> invalidIds() {
        return List.of(
                "00000000000000000000000",
                "0000000000000000000000000",
                "gggggggggggggggggggggggg"
        );
    }
}
