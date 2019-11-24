package model;

import java.util.ArrayList;
import java.util.Collections;
import model.Enums.RoomStatus;
import model.Enums.RoomType;

public abstract class Room {
	
	private final int MAX_RECORD_SIZE = 10;
	private final int MAX_FEATURE_SIZE = 20;
	
	private String id;
	private int numOfBeds;
	private String feature;
	private RoomType type;
	private RoomStatus status;
	private int numOfRentDays;
	protected ArrayList<HiringRecord> rentRecords;
	private String image;
	
	public Room(String id, int numOfBeds, String feature, RoomType type, RoomStatus status, String image) {
		setId(id);
		setNumOfBeds(numOfBeds);
		setFeature(feature);
		setType(type);
		setStatus(status);
		setNumOfRentDays(0);
		rentRecords = new ArrayList<HiringRecord>();
		setImage(image);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumOfBeds() {
		return numOfBeds;
	}

	public void setNumOfBeds(int numOfBeds) {
		this.numOfBeds = numOfBeds;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		if (feature == null || feature.isEmpty()) {
			this.feature = "Information not available at the moment";
		}
		
		String[] words = feature.split(",\\s+|\\s+|,");
		// Feature summary should be a short string (maximum 20 words)
		if (words.length > MAX_FEATURE_SIZE) {
			this.feature = "";
			for (int i = 0; i < MAX_FEATURE_SIZE; i++) {
				this.feature += words[i];
				if (i != MAX_FEATURE_SIZE - 1)
					this.feature += ", ";
			}
		}
		else
			this.feature = feature;
	}

	public RoomType getType() {
		return type;
	}

	public void setType(RoomType type) {
		this.type = type;
	}

	public RoomStatus getStatus() {
		return status;
	}

	public void setStatus(RoomStatus status) {
		this.status = status;
	}
	
	public int getNumOfRentDay() {
		return numOfRentDays;
	}

	public void setNumOfRentDays(int numOfRentDays) {
		this.numOfRentDays = numOfRentDays;
	}
	
	public HiringRecord getCurrentRecord() {
		return rentRecords.get(rentRecords.size() - 1);
	}

	public void setNewRentRecord(HiringRecord rentRecord) {
		if (rentRecords.size() == MAX_RECORD_SIZE) {
			Collections.rotate(rentRecords, -1);
			rentRecords.remove(MAX_RECORD_SIZE - 1);
			rentRecords.add(MAX_RECORD_SIZE - 1, rentRecord);
		}
		else
			rentRecords.add(rentRecord);
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	// implements methods such as rent(), return() 
	// maintenance() completeMaintenance()
	public void rent(String customerId, DateTime rentDate, int numOfRentDays) throws RentException {
		// check room status and throw exception if it is not available
    	if (getStatus() == RoomStatus.Available) {
    		setStatus(RoomStatus.Rented);
			setNumOfRentDays(numOfRentDays);
			
			// create hiring record id
			String id = getId() + "_" + customerId + "_" + rentDate.getEightDigitDate();
			// calculate the estimated return date.
			DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDays);
			
			//add new hiring record into room records
			HiringRecord rentRecord = new HiringRecord(id, rentDate, estimatedReturnDate);
			setNewRentRecord(rentRecord);
    	} else {
    		throw new RentException(String.format("%s %s is not available at the moment as it is %s", getType() == RoomType.Suite ? "Suite " : "Room ", getId(), getStatus() == RoomStatus.Rented ? "currently rented." : "under maintenance."));
    	}
	}
	
	public abstract void returnRoom(DateTime returnDate) throws ReturnException;
	
    public void performMaintenance() throws MaintenanceException {
    	// check room status and throw exception if it is not available
    	if (getStatus() == RoomStatus.Available) {
    		setStatus(RoomStatus.Maintenance);
    	} else {
    		throw new MaintenanceException(String.format("Maintenance of %s %s cannot be performed as it is %s", getType() == RoomType.Suite ? "Suite " : "Room ", getId(), getStatus() == RoomStatus.Rented ? "currently rented." : "already under maintenance."));
    	}
    }
    
    public abstract void completeMaintenance(DateTime completionDate) throws MaintenanceException;
	
	public String toString() {
		return getId() + ":" + getNumOfBeds() + ":" + getType() + ":" + getStatus() + ":" + getFeature();
	}
	
	protected String getDetails() {
		String roomDetails = null;
		roomDetails = "Room ID:		" + getId();
		roomDetails += System.lineSeparator() + "Number of beds:		" + getNumOfBeds();
		roomDetails += System.lineSeparator() + "Type:			" + getType();
		roomDetails += System.lineSeparator() + "Status:			" + getStatus();
		roomDetails += System.lineSeparator() + "Feature Summary:	" + getFeature();
    	
    	if (this.rentRecords.size() == 0)
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
	
	protected String latestRentalDetails() {
		String latestRentalDetails = null;
		latestRentalDetails = "Room ID:		" + getId();
		latestRentalDetails += System.lineSeparator() + "Number of beds:		" + getNumOfBeds();
		latestRentalDetails += System.lineSeparator() + "Type:			" + getType();
		latestRentalDetails += System.lineSeparator() + "Status:			" + getStatus();
		latestRentalDetails += System.lineSeparator() + "Feature Summary:	" + getFeature();
		latestRentalDetails += System.lineSeparator() + "RENTAL RECORD";
		latestRentalDetails += System.lineSeparator() + rentRecords.get(rentRecords.size() - 1).getDetails();
    	return latestRentalDetails;
	}
}
