package com.hk.mm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 406
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ServiceNotAcceptable extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ServiceNotAcceptable() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ServiceNotAcceptable(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceNotAcceptable(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ServiceNotAcceptable(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServiceNotAcceptable(Throwable cause) {
		super(cause);
	}

}
