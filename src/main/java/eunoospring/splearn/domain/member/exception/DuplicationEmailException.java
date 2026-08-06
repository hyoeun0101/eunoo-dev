package eunoospring.splearn.domain.member.exception;

public class DuplicationEmailException extends RuntimeException {
    public DuplicationEmailException(String message) {
        super(message);
    }
}
