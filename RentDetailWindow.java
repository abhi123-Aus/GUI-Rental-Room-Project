package view;
import controller.*;
import model.*;
import model.Enums.RoomType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RentDetailWindow{

	private Button dialogOKButton = new Button("OK");
	private Button dialogCancelButton = new Button("Cancel");
	private Stage dialogBox = new Stage();
	private Room room;
	private String enteredRentDate;
	private DateTime rentDate;
	private int numOfRentDays;
	private boolean numeric = true;
	String message = "";
	final private String pattern = "dd/MM/yyyy"; 
	
	public RentDetailWindow(Room r) {
		room = r;
		dialogBox.setTitle("Rental details registration");
		
		// Rental date
		Label Datelabel = new Label("What is the rental date?");
		DatePicker datePicker = new DatePicker();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		Locale.setDefault(Locale.ENGLISH);

	    HBox datebox = new HBox(datePicker);
		
	    // Customer
	    Label Customerlabel = new Label("Please enter a Customer Id:");
		TextField CustomerField = new TextField();
		CustomerField.setPromptText("Name");
		CustomerField.setId("CustomerId");
	    
	    
	    // Rental days
		Label Dayslabel = new Label("How many days?");
		TextField DaysField = new TextField("0");
		try{
			int num = Integer.parseInt(DaysField.getText());
			numeric = true;
		} catch(NumberFormatException error) {
			numeric = false;
			throw new RentException("You must select a valid number of days.");
		}
			
		DaysField.setPromptText("Days");
		DaysField.setId("Days");
		
		// Dialog boxes
		HBox dialogButtons = new HBox();
		Label messagebox = new Label("");
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
	
		
		// Layout the dialog components
		VBox dialogVBox = new VBox();
		dialogVBox.getChildren().addAll(Customerlabel, CustomerField, Datelabel, datebox, Dayslabel, DaysField, messagebox, dialogButtons);
		dialogVBox.setPadding(new Insets(10, 10, 10, 10));
		
		dialogCancelButton.setOnAction(new DialogCancelController(dialogBox));
		dialogOKButton.setOnAction(
				(e) -> {
					try{
						int num = Integer.parseInt(DaysField.getText());
						numeric = true;
					} catch(NumberFormatException error) {
						numeric = false;
					}

					if ((numeric == false) || (DaysField.getText() == "0")) {
						message = "Please select a date and enter number of days to rent (must be at least 1 day).";
						messagebox.setText(message);
						messagebox.setWrapText(true);
					} else {
						numOfRentDays = Integer.parseInt(DaysField.getText());
						enteredRentDate = datePicker.getValue().format(formatter);
						int day = Integer.parseInt(enteredRentDate.substring(0,2));
						int month = Integer.parseInt(enteredRentDate.substring(3,5));
						int yr = Integer.parseInt(enteredRentDate.substring(6,10));
						rentDate = new DateTime(day, month, yr);
						
						// Rents the room if user input is valid and refreshes the database.
						if (Model.getCurrentInstance().getValues().contains(r)) {
							try {
								r.rent(CustomerField.getText(), rentDate, numOfRentDays);
								DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDays);
								messagebox.setText(String.format("%s %s is now rented by customer: %s. \nPlease return the room by: %s, %s", r.getType() == RoomType.Suite ? "Suite " : "Room ", r.getId(), CustomerField.getText(), estimatedReturnDate.getNameOfDay(), estimatedReturnDate));
							} catch (RentException re) {
								messagebox.setText(re.getErrorMsg());
							}
							
							messagebox.setWrapText(true);
							dialogCancelButton.setDisable(true);
							dialogOKButton.setOnAction(new DialogOKController(dialogBox));
							System.out.println(r.getStatus());
							
							// After changing room status, updates system and refreshes the database
							ConnectionSetup.UpdateRoom(r.getId(), "status", r.getStatus().toString());
							ConnectionSetup.InsertHiringRecord(CustomerField.getText(), room.getCurrentRecord().getRentDate().getEightDigitDate(), room.getCurrentRecord().getEstimatedReturnDate().getEightDigitDate());
							ConnectionSetup.refreshRoom();
							ConnectionSetup.refreshHiringRecord();
						}
					}
					
				}
			);
		
		Scene dialogScene = new Scene(dialogVBox, 200, 300);
		dialogBox.setScene(dialogScene);
	}
	
	public Stage getDialogBox() {
		return dialogBox;
	}
}
