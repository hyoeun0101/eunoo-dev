package eunoospring.splearn.domain.curriculum;

public class InvalidCurriculumException  extends RuntimeException{
    public InvalidCurriculumException() {
        super();
    }

    public InvalidCurriculumException(String message) {
        super(message);
    }

    public InvalidCurriculumException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCurriculumException(Throwable cause) {
        super(cause);
    }

    protected InvalidCurriculumException(String message, Throwable cause, boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
