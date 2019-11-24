package view;

import controller.*;
import model.*;
import model.Enums.RoomType;
import java.util.InputMismatchException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class PerformMaintenanceWindow {

	private Button dialogOKButton = new Button("OK");
	private Stage dialogBox = new Stage();
	
	public PerformMaintenanceWindow(Room room) {
		String message;
		dialogBox.setTitle("Maintenance");
		
		BorderPane BorderPane = new BorderPane(); 
		BorderPane.setPadding(new Insets(5, 5, 5, 5));
		
		try {
			room.performMaintenance();
			message = String.format("%s %s has now been placed to undergo maintenance.", room.getType() == RoomType.Suite ? "Suite " : "Room ", room.getId());
		} catch (MaintenanceException me) {
			message = me.getErrorMsg();
		}

		System.out.println(room.getStatus().toString());
		ConnectionSetup.UpdateRoom(room.getId(), "status", room.getStatus().toString());
		ConnectionSetup.refreshRoom();
		ConnectionSetup.refreshHiringRecord();
		
		Text text1 = new Text(20, 20, message);
		text1.setFont(Font.font("Courier", FontWeight.BOLD, 15));

		// OK
		HBox dialogButtons = new HBox();
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
	
		BorderPane.setTop(text1);
		BorderPane.setBottom(dialogButtons);
		
		dialogOKButton.setOnAction(new DialogOKController(dialogBox));
			
		Scene scene = new Scene(BorderPane);
		dialogBox.setScene(scene);

	}
	
	public Stage getDialogBox() {
		return dialogBox;
	}
}
