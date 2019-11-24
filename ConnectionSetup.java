package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Enums.RoomStatus;
import model.Enums.RoomType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Scanner;

public class ConnectionSetup {
	final static String DB_NAME = "CITYLODGE_DB";
    final static String TABLE_NAME1 = "ROOM";
    final static String TABLE_NAME2 = "HIRING_RECORD";
    	
    public static void start() {
    	//use try-with-resources Statement
    	try (Connection con = getConnection(DB_NAME)) {
    		System.out.println("Connection to database " + DB_NAME + " created successfully");
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
    
    public static void checkTable(String table) {
    	// use try-with-resources Statement
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME)) {
    		DatabaseMetaData dbm = con.getMetaData();
			ResultSet tables = dbm.getTables(null, null, table.toUpperCase(), null);
			
			if(tables != null) {
				if (tables.next()) {
					System.out.println("Table " + table + " exists.");
				}
				else {
					System.out.println("Table " + table + " does not exist.");
				}	
				tables.close();
			} else {
				System.out.println("Problem with retrieving database metadata");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    public static void deleteTable(String table) {
    	// use try-with-resources Statement
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
    		int result = stmt.executeUpdate("DROP TABLE " + table);
			
			if(result == 0) {
				System.out.println("Table " + table + " has been deleted successfully");
			} else {
				System.out.println("Table " + table + " was not deleted");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }

    //Create Room table.
    public static void createTableRoom() {
    	//use try-with-resources Statement
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			int result = stmt.executeUpdate("CREATE TABLE room ("
					+ "id VARCHAR(5) NOT NULL,"
					+ "numOfBeds INT NOT NULL,"
					+ "type VARCHAR(8) NOT NULL,"
					+ "status VARCHAR(11) NOT NULL,"
					+ "feature VARCHAR(100) NOT NULL,"
					+ "lastMaintanenceDate VARCHAR(20) NULL,"
					+ "image VARCHAR(20) NULL,"
					+ "PRIMARY KEY (id))");
			if(result == 0) {
				System.out.println("Table " + TABLE_NAME1 + " has been created successfully");
			} else {
				System.out.println("Table " + TABLE_NAME1 + " is not created");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
	
    //Create Hiring Record table.
    public static void createTableHiringRecord() {
    	//use try-with-resources Statement
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			int result = stmt.executeUpdate("CREATE TABLE hiring_record ("
					+ "recordId VARCHAR(40) NOT NULL,"
					+ "rentDate VARCHAR(20) NOT NULL," 
					+ "estimatedReturnDate VARCHAR(15) NULL,"
					+ "actualReturnDate VARCHAR(20) NULL,"
					+ "rentalFee DECIMAL NULL,"
					+ "lateFee DECIMAL NULL,"
					+ "PRIMARY KEY (recordId))");
			if(result == 0) {
				System.out.println("Table " + TABLE_NAME2 + " has been created successfully");
			} else {
				System.out.println("Table " + TABLE_NAME2 + " is not created");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
    }
    
    //Queries some elements of the Room table.
    public static void Query() {
    	//use try-with-resources Statement
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME1;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while(resultSet.next()) {
					System.out.printf("Room ID: %s | Number of beds: %d | Type: %s | Status: %s | Feature Summary: %s | Image: %s", resultSet.getString("id"), resultSet.getInt("numOfBeds"), resultSet.getString("type"), resultSet.getString("status"), resultSet.getString("feature"), resultSet.getString("image"));
					System.out.println();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    //Queries some values of the Hiring_Record table.
    public static void Query2() {
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME2;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while(resultSet.next()) {
					System.out.printf("Record ID: %s | Rent Date: %s | Estimated Return Date: %s | Actual Return Date: %s", resultSet.getString("recordId"), resultSet.getString("rentDate"), resultSet.getString("estimatedReturnDate"), resultSet.getString("actualReturnDate"));
					System.out.println();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    //Queries Room and returns a list
    public static ObservableList<String> QueryRoom() {
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME1;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				ObservableList <String> currentDB = FXCollections.observableArrayList();
				while(resultSet.next()) {
					currentDB.add(resultSet.getString("id") + ":" + resultSet.getInt("numOfBeds") + ":" + resultSet.getString("type") + ":" + resultSet.getString("status") + ":" + resultSet.getString("lastMaintanenceDate") + ":" + resultSet.getString("feature") + ":" + resultSet.getString("image"));
				}
				return currentDB;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    //Queries Hiring_Record and returns a list
    public static ObservableList<String> QueryHiringRecord() {
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME2;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				ObservableList <String> currentDB = FXCollections.observableArrayList();
				while(resultSet.next()) {
					currentDB.add(resultSet.getString("recordId") + ":" + resultSet.getString("rentDate") + ":" + resultSet.getString("estimatedReturnDate") + ":" + resultSet.getString("actualReturnDate") + ":" + resultSet.getInt("rentalFee") + ":" + resultSet.getInt("lateFee"));
				}
				return currentDB;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    
    public static Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
        //Registering the HSQLDB JDBC driver
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
            
        /* Database files will be created in the "database"
         * folder in the project. If no username or password is
         * specified, the default SA user and an empty password are used */
        Connection con = DriverManager.getConnection
                ("jdbc:hsqldb:file:database/" + dbName, "SA", "");
        return con;
    }
    
    public static void UpdateRoom(String roomId, String column, String val) {
    	
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "UPDATE " + TABLE_NAME1 +
					" SET " + column + " = " + "'" + val + "'" +
					" WHERE id LIKE " + "'" + roomId + "'";
			
			int result = stmt.executeUpdate(query);
			
			System.out.println("Update table " + TABLE_NAME1 + " executed successfully");
			System.out.println(result + " row(s) affected");
	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }

    public static void UpdateHiringRecord(String recordId, String col, String val) {
    	String id = "'" + recordId + "'";
    	String column = col;
    	
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "UPDATE " + TABLE_NAME2 +
					" SET " + column + "= " + "'" + val + "'" +
					" WHERE recordId LIKE "+ id;
			
			int result = stmt.executeUpdate(query);
			
			System.out.println("Update table " + TABLE_NAME2 + " executed successfully");
			System.out.println(result + " row(s) affected");
	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    public static void InsertHiringRecord(String recordId, String rentDate, String estimatedReturnDate) { 
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = String.format("INSERT INTO " + TABLE_NAME2 + " VALUES ('%s', '%s', '%s', '%s', %f, %f)", recordId, rentDate, estimatedReturnDate, null, 0.00, 0.00);
			System.out.println(query);
			int result = stmt.executeUpdate(query);
			con.commit();
			System.out.println("Insert into table " + TABLE_NAME2 + " executed successfully");
			System.out.println(result + " row(s) affected");
			System.out.println();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    // Refreshes the current CityLodge system rooms based on the current DB.
    public static void refreshRoom() {
    	Model.getCurrentInstance().getValues().clear();
    	
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME1;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while(resultSet.next()) {
					String id = resultSet.getString("id");
					int numOfBeds = resultSet.getInt("numOfBeds");
					RoomType type = getType(resultSet.getString("type"));
					RoomStatus status = getStatus(resultSet.getString("status"));
					String feature = resultSet.getString("feature");
					DateTime lastMaintanenceDate = convertToDT(resultSet.getString("lastMaintanenceDate"));
					String image = resultSet.getString("image");
	
					if (resultSet.getString("type").equalsIgnoreCase("Standard")) {
						Model.getCurrentInstance().getValues().add(new StandardRoom(id, numOfBeds, feature, type, status, image));
					} else if (resultSet.getString("type").equalsIgnoreCase("Suite")) {
						Model.getCurrentInstance().getValues().add(new Suite(id, feature, type, status, image, lastMaintanenceDate));
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	System.out.println(TABLE_NAME1 + " table refreshed.");
    }
    
  // Refreshes the current CityLodge system records based on the current DB.
    public static void refreshHiringRecord() {
    	ObservableList<Room> currentRoom = Model.getCurrentInstance().getValues();
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME2;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while(resultSet.next()) {
					String roomId = resultSet.getString("recordId").split("\\_")[0] + "_" + resultSet.getString("recordId").split("\\_")[1];
					for (Room a : currentRoom) {
						if (a.getId().equalsIgnoreCase(roomId)) {
							String recordId = resultSet.getString("recordId");
							DateTime rentDate = convertToDT(resultSet.getString("rentDate"));
							DateTime estimatedReturnDate = convertToDT(resultSet.getString("estimatedReturnDate"));
							DateTime actualReturnDate = convertToDT(resultSet.getString("actualReturnDate"));						
							double rentalFee = resultSet.getDouble("rentalFee");
							double lateFee = resultSet.getDouble("lateFee");
							HiringRecord rentRecord = new HiringRecord(recordId, rentDate, estimatedReturnDate, actualReturnDate, rentalFee, lateFee);
							a.setNewRentRecord(rentRecord);
						}
					}
				}
			}
    	} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	System.out.println(TABLE_NAME2 + " table refreshed.");
    }
    
    public static void addtoDB() throws FileNotFoundException {

    	System.out.println("Initializing Database...");
    	final String FILE_NAME = "sampleimport.txt";
    	
    	InputStream inputStream = new FileInputStream(FILE_NAME);
    	Scanner scanner = new Scanner(inputStream);
    	
    	while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] strArr = line.split("\\_");
			String[] strArr2 = line.split("\\:");
			
			// Check first if any value shows 'none'.
			for (int i = 0; i < strArr2.length; i++) {
				if (strArr2[i].equals("none")) {
					strArr2[i] = "0";
				}
			}
			
			// Check first if this is a 'record' by how many underscores the inputstream line has. If > 2, it is a record.
			if ((strArr.length) > 2) {
				String recordId = strArr2[0];	
				String rentDate = strArr2[1];
				String estimatedReturnDate = strArr2[2];
				String actualReturnDate = strArr2[3];
				double rentalFee = Double.parseDouble(strArr2[4]);
				double lateFee = Double.parseDouble(strArr2[5]);
				
				try (Connection con = ConnectionSetup.getConnection(DB_NAME);
						Statement stmt = con.createStatement();
				) {
					String query = String.format("INSERT INTO " + TABLE_NAME2 + " VALUES ('%s', '%s', '%s', '%s', %f, %f)", recordId, rentDate, estimatedReturnDate, actualReturnDate, rentalFee, lateFee);
					System.out.println(query);
					int result = stmt.executeUpdate(query);
					con.commit();
					System.out.println("Insert into table " + TABLE_NAME2 + " executed successfully");
					System.out.println(result + " row(s) affected");
					System.out.println();
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				// Insert data into Room table
			} else {
				String roomId = strArr2[0];
				int numOfBeds = Integer.parseInt(strArr2[1]);
				RoomType type = getType(strArr2[2]);
				RoomStatus status = getStatus(strArr2[3]);
				String lastMaintanenceDate = null;
				if (strArr2[4] != null)
					lastMaintanenceDate = strArr2[4];
				String feature = strArr2[5];
				String image = strArr2[6];
				
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
    	refreshRoom();
    	refreshHiringRecord();
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