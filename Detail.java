package view;
import model.*;

import java.io.File;

import controller.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.image.*;

public class Detail {
	static String currentDirectory = System.getProperty("user.dir");
	static String imageDirectory = currentDirectory + "\\images\\";
	
	private Stage dialogDetail = new Stage();
	private Room room;
	
	public Detail(Button btn, Room room) {
		this.room = room;
		dialogDetail.setTitle("Detail of room");
		
		//Menubar items - Top of borderpane
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem importMenuItem = new MenuItem("Import");
		importMenuItem.setOnAction(new MenuItemListener(importMenuItem, dialogDetail));
		MenuItem exportMenuItem = new MenuItem("Export");
		exportMenuItem.setOnAction(new MenuItemListener(exportMenuItem,  dialogDetail));
		fileMenu.getItems().addAll(importMenuItem, exportMenuItem);
		menuBar.getMenus().add(fileMenu);		
		
		
		//Header and scrollable list with details of room and its hiring records - Middle of border pane
		String strlabel = "";
		if (room instanceof StandardRoom) {
			strlabel = String.format("ROOM DETAILS: \n %s", ((StandardRoom)room).getDetails());
		} else if (room instanceof Suite) {
			strlabel = String.format("ROOM DETAILS: \n %s", ((Suite)room).getDetails());
		}
		Label label = new Label(strlabel);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(label);

		//images
		final ImageView selectedImage = new ImageView(); 
		Image image1 = new Image(new File(imageDirectory+"Suite1.jpg").toURI().toString());
		//ImageView img = new ImageView(profile);
		selectedImage.setImage(image1);
		selectedImage.setFitHeight(50);
		selectedImage.setFitWidth(80);
		
		//Buttons
		Button act0 = new Button("Rent Room");
		Button act1 = new Button("Return Room");
		Button act2 = new Button("Perform Maintenance");
		Button act3 = new Button("Complete Maintenance");
		VBox buttons = new VBox();
		buttons.getChildren().addAll(act0, act1, act2, act3);
		VBox buttonBar = new VBox();
		buttonBar.getChildren().add(buttons);

		//Ok-cancel
		Button dialogOKButton = new Button("OK");
		Button dialogCancelButton = new Button("Cancel");
		HBox dialogButtons = new HBox();
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogButtons.setAlignment(Pos.CENTER);
		VBox dialogVBox = new VBox();
		dialogVBox.getChildren().add(dialogButtons);
		
		//Layout of window
		Insets insets = new Insets(10);
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(15, 12, 15, 12));
		borderPane.setTop(menuBar);
		borderPane.setLeft(selectedImage);
		borderPane.setCenter(scrollPane);
		borderPane.setRight(buttonBar);
		borderPane.setBottom(dialogVBox);
		BorderPane.setAlignment(dialogVBox, Pos.CENTER);
		

		BorderPane.setMargin(menuBar, insets);
		BorderPane.setMargin(selectedImage, insets);
		BorderPane.setMargin(scrollPane, insets);
		BorderPane.setMargin(buttonBar, insets);
		BorderPane.setMargin(dialogVBox, insets);
		
		//getDetails button
		Scene dialogScene = new Scene(borderPane, 550, 400);
		dialogDetail.setScene(dialogScene);
		btn.setOnAction(
				(e) -> {
					dialogDetail.show();
				}
			);
		
		dialogOKButton.setOnAction(new DialogOKController(dialogDetail));
		dialogCancelButton.setOnAction(new DialogCancelController(dialogDetail));
		
		//links to the button listeners.
		act0.setOnAction(new RentController(dialogDetail, room));
		act1.setOnAction(new ReturnController(dialogDetail, room));
		act2.setOnAction(new PerformMaintenanceController(dialogDetail, room));
		act3.setOnAction(new CompleteMaintenanceController(dialogDetail, room));
		
		}

	
	public Stage getDetail() {
		return dialogDetail;
	}
	
	public Room getRoom() {
		return room;
	}
}
	
