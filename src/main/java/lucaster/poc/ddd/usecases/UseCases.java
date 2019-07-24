package lucaster.poc.ddd.usecases;

// https://github.com/jasongoodwin/better-java-monads/blob/master/src/main/java/com/jasongoodwin/monads/Try.java

final class SearchUseCase extends UseCase<SearchRequest, SearchResponse> {
    @Override
    Try<SearchResponse> execute(SearchRequest input) {
        return null;
    }
}
final class SearchRequest extends Request {}
final class SearchResponse extends Response {}


abstract class UseCase<REQ extends Request, RES extends Response> {
    abstract Try<RES> execute(REQ request);
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
    private final Throwable error;
    Fail(Throwable error) {
        this.error = error;
    }
    Throwable getError() {
        return this.error;
    }
}
final class Success<T> extends Try<T> {
    private final T t;
    Success(T t) {
        this.t = t;
    }
    T getT() {
        return this.t;
    }
}
