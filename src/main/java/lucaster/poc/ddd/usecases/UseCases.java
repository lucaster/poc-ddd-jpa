package lucaster.poc.ddd.usecases;

import java.util.Collections;
import java.util.Set;

abstract class UseCase<I extends UseCaseRequest, O extends UseCaseResponse> {
    final public Try<O> run(I request) {
        try {
            validate(request);
        }
        catch (ValidationFailedException e) {
            return Try.failure(e); // User error
        }
        try {
            O response = execute(request);
            return Try.success(response);
        }
        catch (RuntimeException e) {
            return Try.failure(e); // Server error
        }
    }
    protected abstract void validate(I request) throws ValidationFailedException;
    protected abstract O execute(I request);
}
abstract class UseCaseRequest {}
abstract class UseCaseResponse {}
class ValidationFailedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public final ValidationFailure vf;
    public ValidationFailedException(ValidationFailure vf) {
        this.vf = vf;
    }
}
final class ValidationFailure {
    public final Set<ValidationFailedDetail> details;
    public ValidationFailure(Set<ValidationFailedDetail> details) {
        this.details = Collections.unmodifiableSet(details);
    }
}
final class ValidationFailedDetail {}



// https://github.com/jasongoodwin/better-java-monads/blob/master/src/main/java/com/jasongoodwin/monads/Try.java
abstract class Try<T> {
    protected Try() {}
    public static <U> Try<U> success(U u) {
        return new Success<>(u);
    }
    public static <U> Try<U> failure(Throwable e) {
        return new Failure<>(e);
    }
}
final class Failure<T> extends Try<T> {
    public final Throwable error;
    Failure(Throwable error) {
        this.error = error;
    }
}
final class Success<T> extends Try<T> {
    public final T t;
    Success(T t) {
        this.t = t;
    }
}


// @Transactional @Service
class CreateProposal extends UseCase<CreateProposalRequest, CreateProposalResponse> {
    // constructor with injected dependencies
    @Override
    protected void validate(CreateProposalRequest request) throws ValidationFailedException {
    }
    @Override
    protected CreateProposalResponse execute(CreateProposalRequest request) {
        return new CreateProposalResponse();
    }
}
final class CreateProposalRequest  extends UseCaseRequest {}
final class CreateProposalResponse extends UseCaseResponse {}