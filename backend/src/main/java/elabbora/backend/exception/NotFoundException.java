package elabbora.backend.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entity) {
        super(entity + " not found");
    }
}