package model;

import model.Enums.RoomStatus;
import model.Enums.RoomType;

public class StandardRoom extends Room {
	
	// Instance variables required for the 
	// StandardRoom type
	public static final double SINGLE_BED_DAILY_RATE = 59;
	public static final double DOUBLE_BED_DAILY_RATE = 99;
	public static final double FOUR_BED_DAILY_RATE = 199;
	public static final double LATE_FEE_DAILY_PERCENTAGE = 135 / 100;
	
	// Create constructors (minimise code duplication) 
	// by using super(...)
	public StandardRoom(String id, int numOfBeds, String feature, RoomType type, RoomStatus status, String image) {
		super(id, numOfBeds, feature, type, status, image);
	}
	
	// implements methods such as rent(), return() 
	// maintenance() completeMaintenance(), toString, getDetails....
	@Override
	public void rent(String customerId, DateTime rentDate, int numOfRentDays) throws RentException {
		String[] weekday = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
		String[] weekend = {"Friday", "Saturday", "Sunday"};
		String DayName = rentDate.getNameOfDay();
		System.out.println("Room rent method called.");
		boolean rentOK = false;
		
		//Check if rental day is on weekday
		for (String day : weekday) {
			if(day.equals(DayName)) {
				if (numOfRentDays < 2) {
					numOfRentDays = 2;
					super.rent(customerId, rentDate, numOfRentDays);
					throw new RentException("Minimum number of days you can rent is 2. Room will be rented for 2 days.");
				} else {
					super.rent(customerId, rentDate, numOfRentDays);
					rentOK = true;
				}
			}
			if (rentOK)
				break;
		}
		//Check if rental day is on weekend
		for (String day : weekend) {
			if(day.equals(DayName)) {
				if (numOfRentDays < 3) {
					numOfRentDays = 3;
					super.rent(customerId, rentDate, numOfRentDays);
					throw new RentException("Minimum number of days you can rent is 3. Room will be rented for 3 days.");
				} else {
					super.rent(customerId, rentDate, numOfRentDays);
					rentOK = true;
				}
			}
			if (rentOK)
				break;
		}
	}

	@Override
	public void returnRoom(DateTime returnDate) throws ReturnException {
		// check room status and throw exception if it is not under maintenance
    	if (getStatus() == RoomStatus.Rented) {
    		// get the last hiring record
			HiringRecord rentRecord = rentRecords.get(rentRecords.size() - 1);
			
			// check if the return date entered is before the rent date listed in the rooms's records
			if (DateTime.diffDays(returnDate, rentRecord.getRentDate()) < 0) {
				throw new ReturnException("Enter a valid return date.");
			} else {
				double rentalFee = 0.00;
				double lateFee = 0.00;
				double lateDayRentalRate = 0.00;
				
				// calculate the number of rented and late days
				int rentedDays = DateTime.diffDays(rentRecord.getEstimatedReturnDate(), rentRecord.getRentDate());
				int lateDays = DateTime.diffDays(returnDate, rentRecord.getEstimatedReturnDate());
				
				switch(getNumOfBeds()) {
					case 1:
						rentalFee = rentedDays * SINGLE_BED_DAILY_RATE;
						lateDayRentalRate = LATE_FEE_DAILY_PERCENTAGE * SINGLE_BED_DAILY_RATE;
						break;
					case 2:
						rentalFee = rentedDays * DOUBLE_BED_DAILY_RATE;
						lateDayRentalRate = LATE_FEE_DAILY_PERCENTAGE * DOUBLE_BED_DAILY_RATE;
						break;
					case 4:
						rentalFee = rentedDays * FOUR_BED_DAILY_RATE;
						lateDayRentalRate = LATE_FEE_DAILY_PERCENTAGE * FOUR_BED_DAILY_RATE;
						break;
				}
				
				setStatus(RoomStatus.Available);
				setNumOfRentDays(0);
				rentRecord.setActualReturnDate(returnDate);
				rentRecord.setRentalFee(rentalFee);
				
				if (lateDays > 0) {
					lateFee = lateDays * lateDayRentalRate;
					rentRecord.setLateFee(lateFee);
				}
			}
    	} else {
			throw new ReturnException(String.format("Room %s cannot be returned as it is %s", getId(), getStatus() == RoomStatus.Available ? "currently not rented." : "currently under maintenance."));
		}
	}

	@Override
	public void completeMaintenance(DateTime completionDate) throws MaintenanceException {
		// check room status and throw exception if it is not under maintenance
    	if (getStatus() == RoomStatus.Maintenance) {
    		setStatus(RoomStatus.Available);
    	} else {
			throw new MaintenanceException(String.format("Maintenance of Room %s cannot be completed as it is %s", getId(), getStatus() == RoomStatus.Rented ? "currently rented." : "not under maintenance."));
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public String getDetails() {
		return super.getDetails();
	}
}
