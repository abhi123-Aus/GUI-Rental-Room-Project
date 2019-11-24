package model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.io.FileNotFoundException;

public class Model {
	
	private static Model instance;
	final static String Table1 = "ROOM";
	final static String Table2 = "HIRING_RECORD";

	private ObservableList<Room> rooms = FXCollections.observableArrayList();
	
	public void test() {
		
		ConnectionSetup.start();
		ConnectionSetup.checkTable(Table1);
		ConnectionSetup.checkTable(Table2);
		
		// Uncomment to clear the database at the start.
		//ConnectionSetup.deleteTable(Table1);
		//ConnectionSetup.deleteTable(Table2);
		
		ConnectionSetup.createTableRoom();
		System.out.println();
		ConnectionSetup.createTableHiringRecord();
		System.out.println();
		
		// Load existing data for rooms (including hiring records) into database from the "sampleimport.txt" file saved in this project.
		try {
			ConnectionSetup.addtoDB();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// View snapshot of current DB entries
		System.out.println();
		System.out.println("The database currently has:");
		ConnectionSetup.Query();
		ConnectionSetup.Query2();
	}

	private Model() {
	}
	
	public static Model getInstance() {
		if (instance == null) {
			instance = new Model();
		}
		instance.test();
		
		return instance;
	}
	
	public static Model getCurrentInstance() {
		return instance;
	}
	
	public ObservableList<Room> getValues() {
		return this.rooms;
	}
	
}
