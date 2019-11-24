package controller;

import model.*;
import view.*;
import java.util.InputMismatchException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Room;

public class CompleteMaintenanceController implements EventHandler<ActionEvent> {
	private Stage dialogBox;
	private Room room;
	
	public CompleteMaintenanceController(Stage dialogBox, Room room) {
		this.dialogBox = dialogBox;
		this.room = room;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		try {
			CompleteMaintenanceWindow cw = new CompleteMaintenanceWindow(room);
			cw.getDialogBox().show();
		} catch (Exception e) {
			ErrorWindow error = new ErrorWindow(e.getMessage());
			error.getDialogBox().show();
		}
	}
}