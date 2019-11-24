package view;

import model.*;
import model.Enums.RoomStatus;
import model.Enums.RoomType;
import view.MainApp.Cell3;
import controller.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.security.auth.callback.Callback;
import controller.MenuItemListener;
import javafx.application.Application;
import javafx.stage.Stage;
// Beans - for property management
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
// Events
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
// Controls
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
// Layouts
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
// Text
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
// Scenes
import javafx.scene.Scene;
//Observable List
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
// Image
import javafx.scene.image.*;

public class MainApp extends Application{
	
	static String currentDirectory = System.getProperty("user.dir");
	static String imageDirectory = currentDirectory + "\\images\\";

	// Access the current instance of the rental system.
	private Model model = Model.getInstance();
	public Model getModel() {
		return model;
	}
	
	@Override 
	// Override the start method in the Application class
	public void start(Stage primaryStage)  throws FileNotFoundException {
		
		// Menu bar
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		
		MenuItem importMenuItem = new MenuItem("Import");
		importMenuItem.setOnAction(new MenuItemListener(importMenuItem, primaryStage));
		
		MenuItem exportMenuItem = new MenuItem("Export");
		exportMenuItem.setOnAction(new MenuItemListener(exportMenuItem, primaryStage));
		
		MenuItem saveToDBMenuItem = new MenuItem("Save to Database");
		saveToDBMenuItem.setOnAction(new MenuItemListener(saveToDBMenuItem, primaryStage));
		
		fileMenu.getItems().addAll(importMenuItem, exportMenuItem, saveToDBMenuItem);
		menuBar.getMenus().add(fileMenu);		

		VBox vBox = new VBox(); 
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
		
		// Buttons
		Button quitButton = new Button("Quit");
		HBox hBox = new HBox(); // Hold two buttons in a vBox
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().add(quitButton);

		// Header
		Label label = new Label("CityLodge");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		
		vBox.getChildren().add(label);
	
		TextField newValueToAdd = new TextField();
		vBox.getChildren().add(newValueToAdd);

		// Create a list view of the rooms in the system.
		ListView<Room> listView = new ListView<Room>();
		listView.setItems(model.getValues());
		listView.setCellFactory(param -> new Cell3());
		vBox.getChildren().add(listView);                      
		vBox.getChildren().add(hBox);
	
		quitButton.setOnAction(
			(e) -> {
					System.out.println("Goodbye!");
					System.exit(0);
				}
		);
		
		//Create the border pane to contain all nodes then set stage and scene.
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menuBar);
		borderPane.setCenter(vBox);
		BorderPane.setAlignment(vBox, Pos.CENTER);
		Scene scene = new Scene(borderPane, 1000, 1000);
		primaryStage.setTitle("CityLodge Rental System"); 
		primaryStage.setScene(scene); 
		
		// Display the stage
		primaryStage.show(); 
	}
	
	
	// Customized Cell contents with brief details about the room.
	static class Cell3 extends ListCell<Room> {
		
		HBox hbox1 = new HBox();
		Button btn = new Button("Get Details");
		Label label = new Label("");
		Pane pane = new Pane();
		
		Image profile = new Image(new File(imageDirectory+"Suite3.jpg").toURI().toString());
		ImageView img = new ImageView(profile);
		
		// This contains the room
		Detail detailWindow;
		
		public Cell3() {
			super();
			img.setFitHeight(100);
			img.setFitWidth(180);
			hbox1.getChildren().addAll(img, label, pane, btn);
			hbox1.setHgrow(pane, Priority.ALWAYS);
			
		}
		
		// Updates the cells as data on rooms are changed in the system and database.
		@Override
		public void updateItem(Room r, boolean empty) {
			super.updateItem(r, empty);;
			setText(null);
			setGraphic(null);
			detailWindow = new Detail(btn, r);
			if(r != null && !empty) {
				String roomId = r.getId();
				int numOfBeds = r.getNumOfBeds();
				RoomType type = r.getType();
				RoomStatus status  = r.getStatus();
				label.setText(roomId + "\n" + "Number of beds: " + numOfBeds + "\n" + "Type: " + type + "\n" + "Status: " + status + "\n");
				setGraphic(hbox1);
			}
		}
			
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
