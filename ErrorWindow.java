package model;

import controller.DialogOKController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorWindow {
	private Button dialogOKButton = new Button("OK");
	private Stage dialogBox = new Stage();
	private Room room;
	private String message;
	
	public ErrorWindow(String desc) {
		dialogBox.setTitle("Error");
		
		BorderPane BorderPane = new BorderPane(); 
		BorderPane.setPadding(new Insets(5, 5, 5, 5));
			
		message = desc;

		Text text1 = new Text(20, 20, message);
		text1.setFont(Font.font("Courier", FontWeight.BOLD, 15));

		HBox dialogButtons = new HBox();
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
			
		BorderPane.setTop(text1);
		BorderPane.setBottom(dialogButtons);
			
		dialogOKButton.setOnAction(new DialogOKController(dialogBox));
			
		Scene scene = new Scene(BorderPane);
		dialogBox.setTitle("Error!"); 
		dialogBox.setScene(scene);
	}
	
	public Stage getDialogBox() {
		return dialogBox;
	}
}