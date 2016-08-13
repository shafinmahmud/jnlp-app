package shafin.web.crawler.parser;

public class UnwantedPageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnwantedPageException() {

	}

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
