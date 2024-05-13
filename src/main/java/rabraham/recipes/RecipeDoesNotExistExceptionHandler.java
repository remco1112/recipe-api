package rabraham.recipes;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {RecipeDoesNotExistException.class, ExceptionHandler.class})
public class RecipeDoesNotExistExceptionHandler implements ExceptionHandler<RecipeDoesNotExistException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, RecipeDoesNotExistException exception) {
        return HttpResponse.notFound();
    }
}
