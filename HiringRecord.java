package model;

import model.DateTime;

public class HiringRecord {
	
	private String id;
	private DateTime rentDate;
	private DateTime estimatedReturnDate;
	private DateTime actualReturnDate;
	private double rentalFee;
	private double lateFee;
	
	public HiringRecord(String id, DateTime rentDate, DateTime estimatedReturnDate) 
	{
		setId(id);
		setRentDate(rentDate);
		setEstimatedReturnDate(estimatedReturnDate);
		setActualReturnDate(null);
		setRentalFee(0.00);
		setLateFee(0.00);
	}
	
	public HiringRecord(String id, DateTime rentDate, DateTime estimatedReturnDate, DateTime actualReturnDate, double rentalFee, double lateFee) 
	{
		setId(id);
		setRentDate(rentDate);
		setEstimatedReturnDate(estimatedReturnDate);
		setActualReturnDate(actualReturnDate);
		setRentalFee(rentalFee);
		setLateFee(lateFee);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public DateTime getRentDate() {
		return rentDate;
	}

	public void setRentDate(DateTime rentDate) {
		this.rentDate = rentDate;
	}
	
	public DateTime getEstimatedReturnDate() {
		return estimatedReturnDate;
	}

	public void setEstimatedReturnDate(DateTime estimatedReturnDate) {
		this.estimatedReturnDate = estimatedReturnDate;
	}
	
	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(DateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public double getRentalFee() {
		return rentalFee;
	}

	public void setRentalFee(double rentalFee) {
		this.rentalFee = rentalFee;
	}

	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}
	
	// Override the toString() method
	public String toString()
	{
		return getId() + ":" + getRentDate() + ":" + getEstimatedReturnDate() + ":" + (getActualReturnDate() == null ? "none" : getActualReturnDate()) + ":" + (getRentalFee() == 0.00 ? "none" : getRentalFee()) + ":" + (getLateFee() == 0.00 ? "none" : getLateFee());
	}
	
	public String getDetails()
	{
		String recordDetails = null;
		recordDetails = "Record ID:		" + getId();
		recordDetails += System.lineSeparator() + "Rent Date:		" + getRentDate();
		recordDetails += System.lineSeparator() + "Estimated Return Date:	" + getEstimatedReturnDate();
		if (getActualReturnDate() != null)
			recordDetails += System.lineSeparator() + "Actual Return Date:	" + getActualReturnDate();
		if (getRentalFee() != 0.00)
			recordDetails += System.lineSeparator() + "Rental Fee:		" + getRentalFee();
		if (getLateFee() != 0.00)
			recordDetails += System.lineSeparator() + "Late Fee:		" + getLateFee();
		return recordDetails;
	}
	
}
 