package eunoospring.splearn.application.instructor.provided;

public class DuplicateInstructorApplicationException extends RuntimeException {
    public DuplicateInstructorApplicationException() {
        super();
    }

    public DuplicateInstructorApplicationException(String message) {
        super(message);
    }

    public DuplicateInstructorApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateInstructorApplicationException(Throwable cause) {
        super(cause);
    }

    protected DuplicateInstructorApplicationException(String message, Throwable cause, boolean enableSuppression,
                                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
