package lucaster.poc.ddd.usecases;

import java.util.Collections;
import java.util.Set;

// https://github.com/jasongoodwin/better-java-monads/blob/master/src/main/java/com/jasongoodwin/monads/Try.java

abstract class UseCase<REQ extends Request, RES extends Response> {
    final public Try<RES> run(REQ request) {
        try {
            validate(request);
        }
        catch (ValidationFailedException e) {
            return Try.failure(e);
        }
        try {
            RES response = execute(request);
            return Try.success(response);
        }
        catch (RuntimeException e) {
            return Try.failure(e);
        }
    }
    protected abstract void validate(REQ request) throws ValidationFailedException;
    protected abstract RES execute(REQ request);
}
final class ValidationFailed {
    public final Set<ValidationFailedDetail> details;
    ValidationFailed(Set<ValidationFailedDetail> details) {
        this.details = Collections.unmodifiableSet(details);
    }
}
final class ValidationFailedDetail {}
class ValidationFailedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public final ValidationFailed vf;
    ValidationFailedException(ValidationFailed vf) {
        this.vf = vf;
    }
}
abstract class Request {}
abstract class Response {}

abstract class Try<T> {
    protected Try() {}
    static <U> Try<U> success(U u) {
        return new Success<>(u);
    }
    public static <U> Try<U> failure(Throwable e) {
        return new Fail<>(e);
    }
}
final class Fail<T> extends Try<T> {
    public final Throwable error;
    Fail(Throwable error) {
        this.error = error;
    }
}
final class Success<T> extends Try<T> {
    public final T t;
    Success(T t) {
        this.t = t;
    }
}



class SearchUseCase extends UseCase<SearchRequest, SearchResponse> {
    // constructor with injected dependencies
    @Override
    protected void validate(SearchRequest request) throws ValidationFailedException {
    }
    @Override
    protected SearchResponse execute(SearchRequest request) {
        return new SearchResponse();
    }
}
final class SearchRequest extends Request {}
final class SearchResponse extends Response {}