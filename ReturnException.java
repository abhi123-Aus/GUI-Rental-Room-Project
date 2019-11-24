package model;

public class ReturnException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ReturnException(String message) {
		this.message = message;
	}
	
	public String getErrorMsg() {
		return message;
	}
}
