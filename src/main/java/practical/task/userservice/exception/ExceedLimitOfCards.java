package practical.task.userservice.exception;

public class ExceedLimitOfCards extends RuntimeException{
    public ExceedLimitOfCards(String message) {
        super(message);
    }
}
