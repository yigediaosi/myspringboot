package com.net.yoga.common.exception;

import lombok.Getter;
import lombok.Setter;

public class BusinessException extends RuntimeException {
	@Getter
	@Setter
	private int returnCode;

	public BusinessException()
	{
		super();
	}

	public BusinessException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public BusinessException(String message)
	{
		super(message);
	}

	public BusinessException(Throwable cause)
	{
		super(cause);
	}

	public BusinessException(Object message)
	{
		super(message.toString());
	}

	public BusinessException(int returnCode) {
		super();
		this.returnCode = returnCode;
	}

	public BusinessException(Exception e, int returnCode) {
		super(e);
		this.returnCode = returnCode;
	}

	public BusinessException(String message, int returnCode) {
		super(message);
		this.returnCode = returnCode;
	}

	public BusinessException(String message, Exception e, int returnCode) {
		super(message, e);
		this.returnCode = returnCode;
	}
}
