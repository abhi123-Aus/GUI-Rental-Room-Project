package model;

public class RentException extends NumberFormatException {
	private static final long serialVersionUID = 1L;
	private String message;

	public RentException(String message) {
		this.message = message;
	}
	
	public String getErrorMsg() {
		return message;
	}

}
