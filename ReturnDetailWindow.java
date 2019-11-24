package view;

import model.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import controller.DialogCancelController;
import controller.DialogOKController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReturnDetailWindow {

	private Button dialogOKButton = new Button("OK");
	private Button dialogCancelButton = new Button("Cancel");
	private Stage dialogBox = new Stage();
	private DateTime returnDate_;
	private String returnDate;
	String message = "";
	final private String pattern = "dd/MM/yyyy"; 
	
	
	public ReturnDetailWindow(Room room) {
		dialogBox.setTitle("Return details registration");
		
		//Date Handling
		Label Datelabel = new Label("What is the return date?");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		Locale.setDefault(Locale.ENGLISH);
		DatePicker datePicker = new DatePicker();
	    HBox datebox = new HBox(datePicker);
	    Label messagebox = new Label();
	    
	    //OK-Cancel
	    HBox dialogButtons = new HBox();
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
		
		//VBx - main pane.
		VBox dialogVBox = new VBox();
		dialogVBox.getChildren().addAll(Datelabel, datebox, messagebox, dialogButtons);
		dialogVBox.setPadding(new Insets(10, 10, 10, 10));
	
		// Listener
		dialogCancelButton.setOnAction(new DialogCancelController(dialogBox));
		dialogOKButton.setOnAction(
				(e) -> {
					// Handles empty date picker.
				    try {
				    	returnDate = datePicker.getValue().format(formatter);
				    } catch(NullPointerException n) {
				    	try {
							throw new ReturnException("No value in datefield. Please select a date.");
						} catch (ReturnException e1) {
							e1.printStackTrace();
						}
				    }			
					
					if (datePicker.getValue() == null) {
						message = "Please select a date.";
						messagebox.setText(message);
						messagebox.setWrapText(true);

					} else {
	
						returnDate = datePicker.getValue().format(formatter);
						int day = Integer.parseInt(returnDate.substring(0,2));
						int month = Integer.parseInt(returnDate.substring(3,5));
						int yr = Integer.parseInt(returnDate.substring(6,10));
						returnDate_ = new DateTime(day, month, yr);
						
						try {
							room.returnRoom(returnDate_);
							message = String.format("Your rental fee for this trip is: $%f \nYour late fee for this trip is: $%f \nYou have returned room %s on %s", room.getCurrentRecord().getRentalFee(), room.getCurrentRecord().getLateFee(), room.getId(), datePicker.getValue().format(formatter));
						} catch (ReturnException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						ConnectionSetup.UpdateRoom(room.getId(), "status", room.getStatus().toString());
						ConnectionSetup.UpdateHiringRecord(room.getCurrentRecord().getId(), "actualReturnDate", room.getCurrentRecord().getActualReturnDate().getEightDigitDate());
						ConnectionSetup.UpdateHiringRecord(room.getCurrentRecord().getId(), "rentalFee", Double.toString(room.getCurrentRecord().getRentalFee()));
						ConnectionSetup.UpdateHiringRecord(room.getCurrentRecord().getId(), "lateFee", Double.toString(room.getCurrentRecord().getLateFee()));
						ConnectionSetup.refreshRoom();
						ConnectionSetup.refreshHiringRecord();
						
						// Message box will display.
						messagebox.setText(message);
						messagebox.setWrapText(true);
						dialogOKButton.setDisable(true);
						dialogCancelButton.setDisable(true);
					}
				});
	
		Scene dialogScene = new Scene(dialogVBox, 200, 200);
		dialogBox.setScene(dialogScene);
	}
	
	public Stage getDialogBox() {
		return dialogBox;
	}
}
