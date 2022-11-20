package project.report_gen.exceptions;

public class NoSuchReportException extends Throwable {
    public NoSuchReportException(String message) {
        super(message);
    }

    public NoSuchReportException(){}
}
