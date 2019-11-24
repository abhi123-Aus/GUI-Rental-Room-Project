package controller;
import model.*;

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;

import javafx.scene.Scene;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DialogOKController implements EventHandler<ActionEvent> {		
	private Stage dialogBox;
	
	public DialogOKController(Stage dialogBox) {
		this.dialogBox = dialogBox;
	}
	
	@Override
	// Override the handle method
	public void handle(ActionEvent e) {
		System.out.println("Ok!");
		dialogBox.close();
	}
}



