package lucaster.poc.ddd.usecases;

import java.util.Collections;
import java.util.Set;

abstract class UseCase<I extends UseCaseRequest, O extends UseCaseResponse> {

    /**
     * Template method pattern
     */
    final public Try<O> execute(I request) {
        try {
            validateSimple(request);
            validateComplex(request);
            O response = work(request);
            return Try.success(response);
        }
        catch (InvalidRequestException | UnauthorizedOperationException | IllegalStateException e) {
            return Try.failure(e);
        }
        catch (RuntimeException e) {
            return Try.failure(e); // Server error
        }
    }

    /**
     * Template method pattern, Clean Architecture
     */
    final public void execute(I request, UseCaseResponseConsumer<O> responseConsumer) {
        Try<O> outcome = execute(request);
        if (outcome instanceof Success) {
            Success<O> success = (Success<O>) outcome;
            responseConsumer.consume(success.t);
        }
        else if (outcome instanceof Failure) {
            Failure<O> failure = (Failure<O>) outcome;
            throw new RuntimeException(failure.error);
        }
    }

    private void validateSimple(I request) throws InvalidRequestException {
        ValidationResult vr = request.validate();
        if (vr instanceof ValidationFailure) {
            throw new InvalidRequestException((ValidationFailure) vr);
        }
    }

    /**
     * Validate against external data that is not present in the request itself. E.G. user permissions, ACL, etc.
     */
    protected abstract void validateComplex(I request) throws UnauthorizedOperationException, IllegalStateException;

    /**
     * Does the actual heavy work
     */
    protected abstract O work(I request);
}
abstract class UseCaseRequest {
    abstract ValidationResult validate();
}
abstract class UseCaseResponse {}
interface UseCaseResponseConsumer<T extends UseCaseResponse> {
    void consume(T response);
}

abstract class ValidationFailureException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public final ValidationFailure vf;
    public ValidationFailureException(ValidationFailure vf) {
        this.vf = vf;
    }
}
class InvalidRequestException extends ValidationFailureException {
    private static final long serialVersionUID = 1L;
    public InvalidRequestException(ValidationFailure vf) {
        super(vf);
    }
}
class UnauthorizedOperationException extends ValidationFailureException {
    private static final long serialVersionUID = 1L;
    public UnauthorizedOperationException(ValidationFailure vf) {
        super(vf);
    }
}
class IllegalStateException extends ValidationFailureException {
    private static final long serialVersionUID = 1L;
    public IllegalStateException(ValidationFailure vf) {
        super(vf);
    }
}

// TODO static factory methods as in Try
abstract class ValidationResult {}
final class ValidationSuccess extends ValidationResult {
    public final Set<ValidationWarning> details;
    ValidationSuccess() {
        this(Collections.<ValidationWarning>emptySet());
    }
    ValidationSuccess(Set<ValidationWarning> details) {
        this.details = Collections.unmodifiableSet(details);
    }
}
final class ValidationWarning {}
final class ValidationFailure extends ValidationResult {
    public final Set<ValidationFailureDetail> details;
    ValidationFailure(Set<ValidationFailureDetail> details) {
        this.details = Collections.unmodifiableSet(details);
    }
}
final class ValidationFailureDetail {
    public final String errorCode;
    public final String errorMessage;
    ValidationFailureDetail(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}



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
    public CreateProposal(/* inject dependencies here */) {
    }
    @Override
    protected void validateComplex(CreateProposalRequest request) throws ValidationFailureException {
        // NOOP
    }
    @Override
    protected CreateProposalResponse work(CreateProposalRequest request) {
        return new CreateProposalResponse();
    }
}
final class CreateProposalRequest  extends UseCaseRequest {
    @Override
    ValidationResult validate() {
        return new ValidationSuccess();
    }
}
final class CreateProposalResponse extends UseCaseResponse {}
