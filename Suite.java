package model;

import model.Enums.RoomStatus;
import model.Enums.RoomType;

public class Suite extends Room {
	
	// Instance variables required for the 
	// Suite type
	public static final double SUITE_DAILY_RATE = 999;
	public static final double SUITE_LATE_FEE_DAILY_RATE = 1099;
	public static final int NUM_OF_BEDS = 6;
	public static final int MAINTENANCE_INTERVAL = 10;
	
	private DateTime lastMaintenanceDate;

	// Create constructors (minimise code duplication) 
	// by using super(...)
	public Suite(String id, String feature, RoomType type, RoomStatus status, String image, DateTime lastMaintanenceDate) {
		super(id, NUM_OF_BEDS, feature, type, status, image);
		setLastMaintenanceDate(lastMaintanenceDate);
    }
	
	public DateTime getLastMaintenanceDate() {
		return lastMaintenanceDate;
	}

	public void setLastMaintenanceDate(DateTime lastMaintanenceDate) {
		this.lastMaintenanceDate = lastMaintanenceDate;
	}

	// implements methods such as rent(), return() 
	// maintenance() completeMaintenance(), toString, getDetails....
	@Override
	public void rent(String customerId, DateTime rentDate, int numOfRentDays) {
		// calculate the estimated return date.
		DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDays);
		
		// calculate the next maintenance date.
		DateTime nextMaintenanceDate = new DateTime(lastMaintenanceDate, MAINTENANCE_INTERVAL);
		
		//check for violation of maintenance schedule.
		if (DateTime.diffDays(estimatedReturnDate, lastMaintenanceDate) > MAINTENANCE_INTERVAL) {
			throw new RentException(String.format("Suite %s cannot be rented for %d days from rent date %s as it must undergo maintenance on %s", getId(), numOfRentDays, rentDate, nextMaintenanceDate));
		} else {
			if (numOfRentDays > 0 && numOfRentDays <= 10) {
				super.rent(customerId, rentDate, numOfRentDays);
			} else {
				super.rent(customerId, rentDate, 1);
				throw new RentException("Minimum number of days you can rent is 1 day. Suite will be rented for 1 day.");
			}
		}
	}

	@Override
	public void returnRoom(DateTime returnDate) throws ReturnException {
		// check suite status and throw exception if it is not under maintenance
    	if (getStatus() == RoomStatus.Rented) {
    		// get the last hiring record
			HiringRecord rentRecord = rentRecords.get(rentRecords.size() - 1);
			
			// check if the return date entered is before the rent date listed in the room's records
			if (DateTime.diffDays(returnDate, rentRecord.getRentDate()) < 0) {
				throw new ReturnException("Enter a valid return date.");
			} else {
				double rentalFee = 0.00;
				double lateFee = 0.00;
				
				// calculate the number of rented and late days
				int rentedDays = DateTime.diffDays(rentRecord.getEstimatedReturnDate(), rentRecord.getRentDate());
				int lateDays = DateTime.diffDays(returnDate, rentRecord.getEstimatedReturnDate());
				
				rentalFee = rentedDays * SUITE_DAILY_RATE;
				setStatus(RoomStatus.Available);
				setNumOfRentDays(0);
				rentRecord.setActualReturnDate(returnDate);
				rentRecord.setRentalFee(rentalFee);
					
				setStatus(RoomStatus.Available);
				setNumOfRentDays(0);
				rentRecord.setActualReturnDate(returnDate);
				rentRecord.setRentalFee(rentalFee);
				
				if (lateDays > 0) {
					lateFee = lateDays * SUITE_LATE_FEE_DAILY_RATE;
					rentRecord.setLateFee(lateFee);
				}
			}
    	} else {
    		throw new ReturnException(String.format("Room %s cannot be returned as it is %s", getId(), getStatus() == RoomStatus.Available ? "currently not rented." : "currently under maintenance."));
		}	
	}
	
	@Override
	public void completeMaintenance(DateTime completionDate) throws MaintenanceException {
		// check suite status and throw exception if it is not under maintenance
    	if (getStatus() == RoomStatus.Maintenance) {
    		setStatus(RoomStatus.Available);
    		setLastMaintenanceDate(completionDate);
    	} else {
			throw new MaintenanceException(String.format("Maintenance of Suite %s cannot be completed as it is %s", getId(), getStatus() == RoomStatus.Rented ? "currently rented." : "not under maintenance."));
		}
	}

	@Override
	public String toString() {
		return getId() + ":" + getNumOfBeds() + ":" + getType() + ":" + getStatus() + ":" + getLastMaintenanceDate() + ":" + getFeature();
	}
	
	@Override
	public String getDetails() {
		String roomDetails = null;
		roomDetails = "Room ID:		" + getId();
		roomDetails += System.lineSeparator() + "Number of beds:		" + getNumOfBeds();
		roomDetails += System.lineSeparator() + "Type:			" + getType();
		roomDetails += System.lineSeparator() + "Status:			" + getStatus();
		roomDetails += System.lineSeparator() + "Last Maintenance Date:	" + getLastMaintenanceDate();
		roomDetails += System.lineSeparator() + "Feature Summary:	" + getFeature();
    	
    	if (rentRecords.size() == 0)
    		roomDetails += System.lineSeparator() + "RENTAL RECORD:		empty";
    	else {
    		roomDetails += System.lineSeparator() + "RENTAL RECORD";
    		for (int i = rentRecords.size() - 1; i >= 0 ; i--)
    		{
    			roomDetails += System.lineSeparator() + rentRecords.get(i).getDetails();
    			if (i > 0) {
    				roomDetails += System.lineSeparator() + "-----------------------------------";
    			}
    		}
    	}
    	return roomDetails;	
	}

}
