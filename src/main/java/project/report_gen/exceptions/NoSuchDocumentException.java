package project.report_gen.exceptions;

public class NoSuchDocumentException extends Exception {
    public NoSuchDocumentException(String message) {
        super(message);
    }

    public NoSuchDocumentException(){}
}