package de.eridea.rest.database;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import de.eridea.rest.jersey.RestService;
import de.eridea.rest.types.DetailedTask;
import de.eridea.rest.types.Item;

/**
 * Class with hard-coded data instead of a database connection.
 * Mostly for testing purposes.
 * 
 * @author Dieter Schneider
 * @since 18.11.2015
 */
public class HardcodedData implements DBConnection{
	
	private ArrayList<DetailedTask> data = null;
	
	/**
	 * Instantiates a new hardcoded data object.
	 */
	public HardcodedData()
	{
		data = new ArrayList<DetailedTask>();
		initData();
	}

	/* (non-Javadoc)
	 * @see de.eridea.rest.database.DBConnection#selectAllTasks()
	 */
	@Override
	public ArrayList<DetailedTask> selectAllTasks()
	{
		return data;
	}

	/* (non-Javadoc)
	 * @see de.eridea.rest.database.DBConnection#selectTask(int)
	 */
	@Override
	public DetailedTask selectTask(int id)
	{
		DetailedTask returnValue = null;
		
		for(int i=0;i<data.size();i++)
		{
			if(data.get(i).getId() == id)
			{
				returnValue = data.get(i);
			}
		}
		
		return returnValue;
	}

	/* (non-Javadoc)
	 * @see de.eridea.rest.database.DBConnection#insertTask(de.eridea.rest.types.DetailedTask)
	 */
	@Override
	public void insertTask(DetailedTask dt)
	{	
		//Nothing todo, because data is not persistent.
	}

	/* (non-Javadoc)
	 * @see de.eridea.rest.database.DBConnection#updateTaskStatus(int, java.lang.String)
	 */
	@Override
	public boolean updateTaskStatus(int id, String status) throws SQLException,
			ClassNotFoundException {
		
		//Nothing todo, because data is not persistent.
		
		return true;
	}

	/* (non-Javadoc)
	 * @see de.eridea.rest.database.DBConnection#getImage(int)
	 */
	@Override
	public File getImage(String name) {
		
		File returnValue = new File(RestService.imageDirectory + name + ".png");
		
		if(returnValue.exists() && !returnValue.isDirectory()) { 
		    return returnValue;
		}
		else
		{
			return null;
		}
	}
	
	private void initData()
	{
		data.add(new DetailedTask(1234, "Wartung Maschine 1234", 47.852967, 12.124801, 
		 		"open", Arrays.asList(new Item("9876", "Ueberspannungsschutz"), 
				new Item("9875", "Generator")), 83022, "Rosenheim", "Hochschulestr. 1", 
		 		"Wartung", "1. Stock", new String[]{"hammer", "bohrmaschine"}, 
		 		new long[]{123, 456}, "12.12.2015", "12.10.2015"));
		
		data.add(new DetailedTask(1235, "Wartung Maschine 1235", 47.852967, 12.124801, 
		 		"closed", Arrays.asList(new Item("9876", "Ueberspannungsschutz"), 
				new Item("9875", "Generator")), 83022, "Rosenheim", "Hochschulestr. 1", 
		 		"Wartung", "1. Stock", new String[]{"hammer", "bohrmaschine"}, 
		 		new long[]{123, 456}, "12.12.2015", "12.10.2015"));
		
		data.add(new DetailedTask(1236, "Wartung Maschine 1236", 47.852967, 12.124801, 
		 		"open", Arrays.asList(new Item("9876", "Ueberspannungsschutz"), 
				new Item("9875", "Generator")), 83022, "Rosenheim", "Hochschulestr. 1", 
		 		"Wartung", "1. Stock", new String[]{"hammer", "bohrmaschine"}, 
		 		new long[]{123, 456}, "12.12.2015", "12.10.2015"));
	}

	/**
	 * Gets the URI of a document with the specified.
	 *
	 * @param name 		the document name.
	 * @return the document URI
	 */
	@Override
	public File getDocument(String name) {
		
		File returnValue = new File(RestService.documentDirectory + name + ".pdf");
		
		if(returnValue.exists() && !returnValue.isDirectory()) { 
		    return returnValue;
		}
		else
		{
			return null;
		}
	}

}
