package net.shafin.crawler.exceptions;

/**
 * @author Shafin Mahmud
 * @since 7/26/2016
 */
public class UnwantedPageException extends RuntimeException {

    public UnwantedPageException() { }

    public UnwantedPageException(String message) {
        super(message);
    }

    public UnwantedPageException(Throwable cause) {
        super(cause);
    }

    public UnwantedPageException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
