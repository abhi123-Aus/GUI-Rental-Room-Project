package controller;

import model.*;
import model.Enums.RoomStatus;
import model.Enums.RoomType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MenuItemListener implements EventHandler<ActionEvent> {
	private MenuItem menuItem;
	private Stage stage;
	final static String DB_NAME = "CITYLODGE_DB";
    final static String TABLE_NAME1 = "ROOM";
    final static String TABLE_NAME2 = "HIRING_RECORD";
	
	public MenuItemListener(MenuItem menuItem, Stage stage) {
		this.menuItem = menuItem;
		this.stage = stage;
	}
	
	@Override
	public void handle(ActionEvent event) {
		MenuItem mItem = (MenuItem) event.getSource();
		String task = mItem.getText();
		
		if ("Add".equalsIgnoreCase(task)) {
			System.out.println("You have chosen to add.");
		} else if ("Import".equalsIgnoreCase(task)) {
			try {
				importFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("Export".equalsIgnoreCase(task)) {
			try {
				exportFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
    public void exportFile() throws IOException {
    	ObservableList<String> CurrentDB = ConnectionSetup.QueryRoom();
    	ObservableList<String> CurrentDB2 = ConnectionSetup.QueryHiringRecord();
    	
    	final String FILE_NAME = "export_data.txt";
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a location to save file");
       
        //choose folder.
        File selectedDirectory = null;
        if(selectedDirectory == null){
        	selectedDirectory = chooser.showDialog(null);
        }

        File file = new File(selectedDirectory + "/" + FILE_NAME);
        PrintWriter outFile = null;
        
        try {
            outFile = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            return;
        }

        for(int i = 0; i<CurrentDB.size(); i++){
            outFile.println(CurrentDB.get(i));
        }
        for(int i = 0; i<CurrentDB2.size(); i++){
            outFile.println(CurrentDB2.get(i));
        }

        outFile.close();
        System.out.println("File export completed successfully!");
    }

    public void importFile() throws IOException {
    	//choose file
    	FileChooser fileChooser = new FileChooser();
    	File selectedFile = fileChooser.showOpenDialog(stage);
    	
    	InputStream inputStream = new FileInputStream(selectedFile);
    	Scanner scanner = new Scanner(inputStream);
    	
    	while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] strArr = line.split("\\_");
			String[] strArr2 = line.split("\\:");
			for (int i = 0; i < strArr2.length; i++) {
				if (strArr2[i].equals("none")) {
					strArr2[i] = "0";
					System.out.println(strArr2[i]);
				}
			}
			if ((strArr.length) > 2) {
				System.out.println("Record");
				
				String recordId = strArr2[0];	
				DateTime rentDate = convertToDT(strArr2[1]);
				DateTime estimatedReturnDate = convertToDT(strArr2[2]);
				DateTime actualReturnDate = convertToDT(strArr2[3]);
				double rentalFee = Double.parseDouble(strArr2[4]);
				double lateFee = Double.parseDouble(strArr2[5]);
				//HiringRecord r = new HiringRecord(strArr[0], strArr[1],  new DateTime(19, 4, 2019), new DateTime(19, 4, 2019), new DateTime(19, 4, 2019), Double.parseDouble(strArr2[4]), Double.parseDouble(strArr2[5])); 
				
				
				try (Connection con = ConnectionSetup.getConnection(DB_NAME);
						Statement stmt = con.createStatement();
				) {
					String query = String.format("INSERT INTO " + TABLE_NAME2 + " VALUES ('%s', '%s', '%s', '%s', %d, %d)", recordId, rentDate, estimatedReturnDate, actualReturnDate, rentalFee, rentalFee);
					System.out.println(query);
					int result = stmt.executeUpdate(query);
					con.commit();
					System.out.println("Insert into table " + TABLE_NAME2 + " executed successfully");
					System.out.println(result + " row(s) affected");
					System.out.println();
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("Room");
				String roomId = strArr2[0];
				int numOfBeds = Integer.parseInt(strArr2[1]);
				RoomType type = getType(strArr2[2]);
				RoomStatus status = getStatus(strArr2[3]);
				String feature = strArr2[4];
				DateTime lastMaintanenceDate = convertToDT(strArr2[5]);
				String image = strArr2[6];
				System.out.println();
				System.out.println("The database currently has:");
				System.out.println(Model.getCurrentInstance().getValues());
				
				try (Connection con = ConnectionSetup.getConnection(DB_NAME);
						Statement stmt = con.createStatement();
				) {
					String query = String.format("INSERT INTO " + TABLE_NAME1 + " VALUES ('%s', %d, '%s', '%s', '%s', '%s', '%s')", roomId, numOfBeds, type, status, feature, lastMaintanenceDate, image);
					System.out.println(query);
					int result = stmt.executeUpdate(query);
					con.commit();
					System.out.println("Insert into table " + TABLE_NAME1 + " executed successfully");
					System.out.println(result + " row(s) affected");
					System.out.println();
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
    	}  	
    }
    
    public static DateTime convertToDT(String datetime) {
    	DateTime DTdate;
    	int day1 = Integer.parseInt(datetime.substring(0,2));
		int month1 = Integer.parseInt(datetime.substring(3,5));
		int year1 = Integer.parseInt(datetime.substring(6,10));
		return DTdate = new DateTime(day1, month1, year1);
    }
    
    public static RoomType getType(String type) {
    	if (type.equalsIgnoreCase("Standard")) {
			return RoomType.Standard;
		} else if (type.equalsIgnoreCase("Suite")) {
			return RoomType.Suite;
		}
		return null;
    }
    
    public static RoomStatus getStatus(String status) {
    	if (status.equalsIgnoreCase("Available")) {
			return RoomStatus.Available;
		} else if (status.equalsIgnoreCase("Rented")) {
			return RoomStatus.Rented;
		} else if (status.equalsIgnoreCase("Maintenance")) {
			return RoomStatus.Maintenance;
		}
		return null;
    }
}