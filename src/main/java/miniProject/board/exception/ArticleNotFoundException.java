package miniProject.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException() {
        super();
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }

    public ArticleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ArticleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
