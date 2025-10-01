package elabbora.backend.exception;

public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException() {
    super("Invalid token");
  }
}