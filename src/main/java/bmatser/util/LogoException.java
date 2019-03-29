package bmatser.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoException extends Exception {

	private static final long serialVersionUID = -5584579897669397115L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogoException.class);
	
	public LogoException() {
		// TODO Auto-generated constructor stub
	}

	public LogoException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public LogoException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public LogoException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public LogoException(Exception e,String message) {
		super(message);
		LOGGER.error(e.toString());
	}

	public LogoException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
