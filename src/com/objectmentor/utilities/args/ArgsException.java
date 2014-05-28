package com.objectmentor.utilities.args;

public class ArgsException extends Exception {

	public static enum ErrorCode {
		INVALID_ARGUMENT_FORMAT, UNEXPECTED_ARGUMENT, UNKNOWN_ARGUMENT_NAME, INVALID_ARGUMENT_NAME,
		MISSING_STRING, MISSING_INTEGER, INVALID_INTEGER, MISSING_DOUBLE, INVALID_DOUBLE
	}

	private char errorArgumentId = '\0';
	private String errorParameter = null;
	private ErrorCode errorCode;

	public ArgsException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ArgsException(ErrorCode errorCode, char errorArgumentId) {
		this.errorCode = errorCode;
		this.errorArgumentId = errorArgumentId;
	}

	public ArgsException(ErrorCode errorCode, String errorParameter) {
		this.errorCode = errorCode;
		this.errorParameter = errorParameter;
	}

	public ArgsException(ErrorCode errorCode, char errorArgumentId, String errorParameter) {
		this.errorCode = errorCode;
		this.errorParameter = errorParameter;
		this.errorArgumentId = errorArgumentId;
	}

	public char getErrorArgumentId() {
		return errorArgumentId;
	}

	public void setErrorArgumentId(char errorArgumentId) {
		this.errorArgumentId = errorArgumentId;
	}

	public String getErrorParameter() {
		return errorParameter;
	}

	public void setErrorParameter(String errorParameter) {
		this.errorParameter = errorParameter;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		switch (errorCode) {
			case UNEXPECTED_ARGUMENT:
				return String.format("Argument -%c unexpected.", errorArgumentId);
			case MISSING_STRING:
				return String.format("Could not find string parameter for -%c.", errorArgumentId);
			case INVALID_INTEGER:
				return String.format("Argument -%c expects an integer but was '%s'.",
					errorArgumentId, errorParameter);
			case MISSING_INTEGER:
				return String.format("Could not find integer parameter for -%c.", errorArgumentId);
			case INVALID_DOUBLE:
				return String.format("Argument -%c expects a double but was '%s'.",
					errorArgumentId, errorParameter);
			case MISSING_DOUBLE:
				return String.format("Could not find double parameter for -%c.", errorArgumentId);
			case INVALID_ARGUMENT_NAME:
				return String.format("'%c' is not a valid argument name.", errorArgumentId);
			case INVALID_ARGUMENT_FORMAT:
				return String.format("'%s' is not a valid argument format.", errorParameter);
			case UNKNOWN_ARGUMENT_NAME:
				return String.format("Argument '%c' did not appear in schema.", errorArgumentId);
			default:
				return "";
		}
	}

}
