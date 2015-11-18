package de.eridea.rest.database;

import java.util.ArrayList;

import de.eridea.rest.types.DetailedTask;

/**
 * Class with hard-coded data instead of a database connection.
 * Mostly for testing purposes.
 * 
 * @author Dieter Schneider
 * @since 18.11.2015
 */
public class HardcodedData implements DBConnection{

	/* (non-Javadoc)
	 * @see de.eridea.rest.database.DBConnection#selectAllTasks()
	 */
	@Override
	public ArrayList<DetailedTask> selectAllTasks(){
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.eridea.rest.database.DBConnection#selectTask(int)
	 */
	@Override
	public DetailedTask selectTask(int id){
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.eridea.rest.database.DBConnection#insertTask(de.eridea.rest.types.DetailedTask)
	 */
	@Override
	public void insertTask(DetailedTask dt){
		// TODO Auto-generated method stub
		
	}

}
