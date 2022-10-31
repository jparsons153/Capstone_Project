package project.report_gen.exceptions;

public class NoSuchProductException extends Exception {
    public NoSuchProductException(String message) {
        super(message);
    }

    public NoSuchProductException(){}
}
