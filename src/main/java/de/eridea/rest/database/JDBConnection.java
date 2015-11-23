package de.eridea.rest.database;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.eridea.rest.types.DetailedTask;
import de.eridea.rest.types.Item;

/**
 * Class which handles interactions with the PostgreSQL.
 * 
 * @author Dieter Schneider
 * @since 11.10.2015
 */
public class JDBConnection implements DBConnection{
	
	/** Logger instance */
	static final Logger logger = Logger.getRootLogger();
	
	/** PostgreSQL database connection */
	static final String dataBaseLocation = "jdbc:postgresql://localhost:5432/mss";
	
	/** PostgreSQL database user */
	static final String dataBaseUser = "postgres";
	
	/** PostgreSQL database password */
	static final String dataBasePassword = "Eridea42";
	
	/**
	 * Fetch all task from the PostgreSQL Database.
	 *
	 * @return ArrayList<DetailedTask>	List of all Tasks inside the Database.
	 * @throws SQLException Signals that a connection to the DB could not be established.
	 * @throws ClassNotFoundException Signals that the JDBC driver class was not found.
	 */
	@Override
	public ArrayList<DetailedTask> selectAllTasks() throws SQLException, ClassNotFoundException
	{
		ArrayList<DetailedTask> returnValue = new ArrayList<DetailedTask>();
		   
		   Gson gson = new Gson();
		   Connection c = null;
	       Statement stmt = null;
	       
	       
	       Class.forName("org.postgresql.Driver");
	       c = DriverManager.getConnection(dataBaseLocation, dataBaseUser, dataBasePassword);
	       c.setAutoCommit(false);
	         
	       logger.info("Opened database successfully (selectAllTasks)");

	       stmt = c.createStatement();
	       ResultSet rs = stmt.executeQuery( "SELECT * FROM task;" );

	       while(rs.next())
	       {
	    	   Type collectionType = new TypeToken<List<Item>>() {}.getType();
	    	   List<Item> items = gson.fromJson(rs.getString("items"), collectionType);
	         
	    	   returnValue.add(new DetailedTask(rs.getInt("id"), rs.getString("description"), 
	        		 rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("status"), 
	        		 items, rs.getInt("plz"), rs.getString("ort"), rs.getString("strasse"), rs.getString("typ"), 
	        		 rs.getString("information"), gson.fromJson(rs.getString("hilfsmittel"), String[].class), 
	        		 gson.fromJson(rs.getString("images"), long[].class), rs.getString("auftragsfrist"), 
	        		 rs.getString("eingangsdatum")));
	       }

	       rs.close();
	       stmt.close();
	       c.close();
	        
	       
	       return returnValue;
	}

	/**
	 * Fetch a single task from the PostgreSQL database according to its ID.
	 *
	 * @param id 				ID of the task that needs to be fetched.
	 * @return DetailedTask			Fetched Task.
	 * @throws SQLException Signals that a connection to the DB could not be established.
	 * @throws ClassNotFoundException Signals that the JDBC driver class was not found.
	 */
	@Override
	public DetailedTask selectTask(int id) throws SQLException, ClassNotFoundException
	{
		   DetailedTask returnValue = null;
		   
		   Gson gson = new Gson();
		   Connection c = null;
	       Statement stmt = null;
	       
	       
	       Class.forName("org.postgresql.Driver");
	       c = DriverManager.getConnection(dataBaseLocation, dataBaseUser, dataBasePassword);
	       c.setAutoCommit(false);
	         
	       logger.info("Opened database successfully (selectTask)");

	       stmt = c.createStatement();
	       ResultSet rs = stmt.executeQuery( "SELECT * FROM task WHERE id="+id+";" );
	       rs.next();

	       Type collectionType = new TypeToken<List<Item>>() {}.getType();
	       List<Item> items = gson.fromJson(rs.getString("items"), collectionType);
	         
	       returnValue = new DetailedTask(rs.getInt("id"), rs.getString("description"), 
	        		 rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("status"), 
	        		 items, rs.getInt("plz"), rs.getString("ort"), rs.getString("strasse"), rs.getString("typ"), 
	        		 rs.getString("information"), gson.fromJson(rs.getString("hilfsmittel"), String[].class), 
	        		 gson.fromJson(rs.getString("images"), long[].class), rs.getString("auftragsfrist"), 
	        		 rs.getString("eingangsdatum"));
	         
	       rs.close();
	       stmt.close();
	       c.close();
	       
	       return returnValue;
	}
	
	/**
	 * Insert a task into the PostgreSQL database.
	 *
	 * @param dt 		Task, which should be inserted into the Database.
	 * @throws SQLException Signals that a connection to the DB could not be established.
	 * @throws ClassNotFoundException Signals that the JDBC driver class was not found.
	 */
	@Override
	public void insertTask(DetailedTask dt) throws SQLException, ClassNotFoundException
	{
		   Gson gson = new Gson();
		   Connection c = null;
		   Statement stmt = null;
		      
		   Class.forName("org.postgresql.Driver");
		   c = DriverManager.getConnection(dataBaseLocation, dataBaseUser, dataBasePassword);
		   c.setAutoCommit(false);
		      
		   logger.info("Opened database successfully (insertTask)");

		   stmt = c.createStatement();
		   String sql = "INSERT INTO task (status, id, description, strasse, plz, ort, latitude, longitude,"
		         + " typ, information, auftragsfrist, eingangsdatum, items, hilfsmittel, images) "
		         + "VALUES ('"+dt.getStatus()+"', "+dt.getId()+", '"+dt.getDescription()+"', '"
		         +dt.getStrasse()+"', "+dt.getPlz()+", '"+dt.getOrt()+"', "+dt.getLatitude()+", "
		         +dt.getLongitude()+", '"+dt.getType()+"', '"+dt.getInformation()+"', "+dt.getAuftragsfrist()
		         +", "+dt.getEingangsdatum()+", '"+gson.toJson(dt.getItems())+"', '"
		         +gson.toJson(dt.getHilfsmittel())+"', '"+dt.getImages()+"' );";

		   stmt.executeUpdate(sql);

		   stmt.close();
		   c.commit();
		   c.close();
		   
		   logger.info("Task inserted successfully");
	 }
	
	public boolean updateTaskStatus(int id) throws SQLException, ClassNotFoundException{
			Connection c = null;
		   Statement stmt = null;
		      
		   Class.forName("org.postgresql.Driver");
		   c = DriverManager.getConnection(dataBaseLocation, dataBaseUser, dataBasePassword);
		   c.setAutoCommit(false);
		   
		   logger.info("Opened database successfully (updateTaskStatus)");
	       
	       stmt = c.createStatement();
	       
	       String sql = "UPDATE task SET status='planned' WHERE id="+id+";";
	       
	       stmt.executeUpdate(sql);
	       
	       stmt.close();
	       c.commit();
	       c.close();
	       
	       logger.info("Task Status changed to planned");
		   
	       return true;
	}
}
