package de.eridea.rest.jersey;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.eridea.rest.types.DetailedTask;
import de.eridea.rest.types.Item;

public class JDBConnection {

	public DetailedTask selectTask(int id)
	   {
		   DetailedTask returnValue = null;
		   
		   Gson gson = new Gson();
		   Connection c = null;
	       Statement stmt = null;
	       
	       try 
	       {
	    	 Class.forName("org.postgresql.Driver");
	       	 c = DriverManager.getConnection("jdbc:postgresql://192.168.2.105:5432/mss", "postgres", "dieter30989");
	         c.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = c.createStatement();
	         ResultSet rs = stmt.executeQuery( "SELECT * FROM task WHERE id="+id+";" );
	         rs.next();

	         Type collectionType = new TypeToken<List<Item>>() {}.getType();
	         List<Item> items = gson.fromJson(rs.getString("items"), collectionType);
	         
	         returnValue = new DetailedTask(rs.getInt("id"), rs.getString("description"), 
	        		 rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("status"), 
	        		 items, rs.getInt("plz"), rs.getString("ort"), rs.getString("strasse"), rs.getString("typ"), 
	        		 rs.getString("information"), gson.fromJson(rs.getString("hilfsmittel"), String[].class), 
	        		 gson.fromJson(rs.getString("images"), long[].class), rs.getLong("auftragsfrist"), 
	        		 rs.getLong("eingangsdatum"));
	         
	         rs.close();
	         stmt.close();
	         c.close();
	       } 
	       catch ( Exception e ) 
	       {
	         e.getStackTrace();
	       }
	       return returnValue;
	   }
	   
	   public void insertTask(DetailedTask dt)
	   {
		   Gson gson = new Gson();
		   Connection c = null;
		   Statement stmt = null;
		      
		   try 
		   {
		      Class.forName("org.postgresql.Driver");
		      c = DriverManager.getConnection("jdbc:postgresql://192.168.2.105:5432/mss", "postgres", "dieter30989");
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

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
		   } 
		   catch (Exception e) 
		   {
		      e.printStackTrace();
		   }
		   System.out.println("Records created successfully");
	   }
}
