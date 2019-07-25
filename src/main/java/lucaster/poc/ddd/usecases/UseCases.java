package lucaster.poc.ddd.usecases;

import java.util.Collections;
import java.util.Set;

abstract class UseCase<I extends UseCaseRequest, O extends UseCaseResponse> {

    /**
     * Template method pattern
     */
    final public Try<O> execute(I request) {
        try {
            validate(request);
            O response = work(request);
            return Try.success(response);
        }
        catch (ValidationFailureException e) {
            return Try.failure(e); // Request invalid because of User error
        }
        catch (RuntimeException e) {
            return Try.failure(e); // Server error
        }
    }

    private void validate(I request) throws ValidationFailureException {
        validateSimple(request);
        validateComplex(request);
    }

    private void validateSimple(I request) throws ValidationFailureException {
        ValidationResult vr = request.validate();
        if (vr instanceof ValidationFailure) {
            throw new ValidationFailureException((ValidationFailure) vr);
        }
    }

    /**
     * Validate against external data that is not present in the request itself. E.G. user permissions, ACL, etc.
     * @throws ValidationFailureException
     */
    protected abstract void validateComplex(I request) throws ValidationFailureException;

    /**
     * Does the actual heavy work
     */
    protected abstract O work(I request);
}
abstract class UseCaseRequest {
    abstract ValidationResult validate();
}
abstract class UseCaseResponse {}
class ValidationFailureException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public final ValidationFailure vf;
    public ValidationFailureException(ValidationFailure vf) {
        this.vf = vf;
    }
}



abstract class ValidationResult {}
final class ValidationSuccess extends ValidationResult {
    public final Set<ValidationWarning> details;
    public ValidationSuccess(Set<ValidationWarning> details) {
        this.details = Collections.unmodifiableSet(details);
    }
}
final class ValidationWarning {}
final class ValidationFailure extends ValidationResult {
    public final Set<ValidationFailureDetail> details;
    public ValidationFailure(Set<ValidationFailureDetail> details) {
        this.details = Collections.unmodifiableSet(details);
    }
}
final class ValidationFailureDetail {
    public final String errorCode;
    public final String errorMessage;
    public ValidationFailureDetail(String errorCode, String errorMessage) {
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
        return new ValidationSuccess(Collections.<ValidationWarning>emptySet());
    }
}
final class CreateProposalResponse extends UseCaseResponse {}