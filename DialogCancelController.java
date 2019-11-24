package controller;

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;

import javafx.scene.Scene;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DialogCancelController implements EventHandler<ActionEvent> {
	private Stage dialogBox;
	
	public DialogCancelController(Stage dialogBox) {
		this.dialogBox = dialogBox;
	}
	
	@Override
	public void handle(ActionEvent e) {
		System.out.println("Cancel!");
		dialogBox.close();
	}
}