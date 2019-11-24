package view;

import controller.*;
import model.*;
import model.Enums.RoomType;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.InputMismatchException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CompleteMaintenanceWindow {

	private Button dialogOKButton = new Button("OK");
	private Button dialogCancelButton = new Button("Cancel");
	private Stage dialogBox = new Stage();
	//private Room room;
	private DateTime completionDate;
	final private String pattern = "dd/MM/yyyy"; 
	
	public CompleteMaintenanceWindow(Room room) {
		//room = r;
		dialogBox.setTitle("Maintenance");
		
		// Date handling
		Label Datelabel = new Label("Enter date of maintenance:");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		Locale.setDefault(Locale.ENGLISH);
		DatePicker datePicker = new DatePicker();
			
		// Message box
		Label messagebox = new Label();
			
		// OK and cancel dialog boxes
		HBox dialogButtons = new HBox();
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
		
			
		VBox dialogVBox = new VBox();
		dialogVBox.getChildren().addAll(Datelabel, datePicker, messagebox, dialogButtons);
		dialogVBox.setPadding(new Insets(10, 10, 10, 10));
		
			dialogOKButton.setOnAction(
					(e) -> {
						String id = room.getId();
						String maintDate = datePicker.getValue().format(formatter);
						int day = Integer.parseInt(maintDate.substring(0,2));
						int month = Integer.parseInt(maintDate.substring(3,5));
						int year = Integer.parseInt(maintDate.substring(6,10));
						completionDate = new DateTime(day, month, year);
						System.out.println(id);
						
						try {
							room.completeMaintenance(completionDate);
							messagebox.setText(String.format("Maintenance of %s %s has been completed on %s. \nIt is now available for rent.", room.getType() == RoomType.Suite ? "Suite " : "Room ", room.getId(), maintDate));
						} catch (MaintenanceException me) {
							messagebox.setText(me.getErrorMsg());
						}
						
						messagebox.setWrapText(true);
						dialogCancelButton.setDisable(true);
						dialogOKButton.setOnAction(new DialogOKController(dialogBox));
						
						// After changing room status, updates system and refreshes the database
						ConnectionSetup.UpdateRoom(room.getId(), "status", room.getStatus().toString());
						ConnectionSetup.refreshRoom();
						ConnectionSetup.refreshHiringRecord();
					}
				);
			
		dialogCancelButton.setOnAction(new DialogCancelController(dialogBox));
	
		Scene dialogScene = new Scene(dialogVBox, 200, 200);
		dialogBox.setScene(dialogScene);
	}
	
	public Stage getDialogBox() {
		return dialogBox;
	}
}
