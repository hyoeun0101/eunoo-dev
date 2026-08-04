package eunoospring.splearn.domain;

public class DuplicationEmailException extends RuntimeException {
    public DuplicationEmailException(String message) {
        super(message);
    }
}
