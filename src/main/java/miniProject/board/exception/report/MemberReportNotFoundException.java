package miniProject.board.exception.report;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberReportNotFoundException extends RuntimeException {
    public MemberReportNotFoundException() {
        super();
    }

    public MemberReportNotFoundException(String message) {
        super(message);
    }

    public MemberReportNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberReportNotFoundException(Throwable cause) {
        super(cause);
    }

    protected MemberReportNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
