package com.hk.mm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 401
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ServiceUnauthorized extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ServiceUnauthorized() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ServiceUnauthorized(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceUnauthorized(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ServiceUnauthorized(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ServiceUnauthorized(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
