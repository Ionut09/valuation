package co.arbelos.gtm.modules.dataingestor.tools.csv.exceptions;

public class MalformedCSVException extends RuntimeException {
    public MalformedCSVException() {
        super();
    }
    public MalformedCSVException(String message, Throwable cause) {
        super(message, cause);
    }
    public MalformedCSVException(String message) {
        super(message);
    }
    public MalformedCSVException(Throwable cause) {
        super(cause);
    }
}
