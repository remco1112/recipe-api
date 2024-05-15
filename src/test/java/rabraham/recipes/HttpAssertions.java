package rabraham.recipes;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;

import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class HttpAssertions {

    private HttpAssertions() {

    }

    static <E> void assertCreatedWithBody(E expectedBody, HttpResponse<E> response) {
        assertStatusWithBody(expectedBody, HttpStatus.CREATED, response);
    }

    static <E> void assertCreatedWithBody(Consumer<E> bodyVerifier, HttpResponse<E> response) {
        assertStatusWithBody(response, bodyVerifier, HttpStatus.CREATED);
    }

    static <E> void assertOkWithBody(E expectedBody, HttpResponse<E> response) {
        assertStatusWithBody(expectedBody, HttpStatus.OK, response);
    }

    static <E> void assertOkWithBody(Consumer<E> bodyVerifier, HttpResponse<E> response) {
        assertStatusWithBody(response, bodyVerifier, HttpStatus.OK);
    }

    static <E> void assertStatusWithBody(E expectedBody, HttpStatus status, HttpResponse<E> response) {
        assertStatusWithBody(response, (actualBody) -> assertEquals(expectedBody, actualBody), status);
    }

    static <E> void assertStatusWithBody(HttpResponse<E> response, Consumer<E> bodyVerifier, HttpStatus status) {
        final Optional<E> body = response.getBody();

        assertEquals(status, response.status());
        assertTrue(body.isPresent());

        bodyVerifier.accept(body.get());
    }

    static void assertNoContentResponse(HttpResponse<Void> response) {
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

    static void assertNotFoundResponse(HttpResponse<?> response) {
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }
}
