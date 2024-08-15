package miniProject.board.exception.report;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException() {
        super();
    }

    public ReportNotFoundException(String message) {
        super(message);
    }

    public ReportNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ReportNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
