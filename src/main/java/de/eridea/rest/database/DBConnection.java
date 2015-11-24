package de.eridea.rest.database;

import java.sql.SQLException;
import java.util.ArrayList;

import de.eridea.rest.types.DetailedTask;

/**
 * Interface for database interactions.
 * 
 * @author Dieter Schneider
 * @since 18.11.2015
 */
public interface DBConnection {
	
	/**
	 * Fetch all task from the Database.
	 *
	 * @return ArrayList<DetailedTask>	List of all Tasks inside the Database.
	 * @throws SQLException Signals that a connection to the DB could not be established.
	 * @throws ClassNotFoundException Signals that a class was not found.
	 */
	public ArrayList<DetailedTask> selectAllTasks() throws SQLException, ClassNotFoundException;
	
	/**
	 * Fetch a single task from the database according to its ID.
	 *
	 * @param id 				ID of the task that needs to be fetched.
	 * @return DetailedTask		Fetched Task.
	 * @throws SQLException Signals that a connection to the DB could not be established.
	 * @throws ClassNotFoundException Signals that a class was not found.
	 */
	public DetailedTask selectTask(int id) throws SQLException, ClassNotFoundException;
	
	/**
	 * Insert a task into the database.
	 *
	 * @param dt 		Task, which should be inserted into the Database.
	 * @throws SQLException Signals that a connection to the DB could not be established.
	 * @throws ClassNotFoundException Signals that a class was not found.
	 */
	public void insertTask(DetailedTask dt) throws SQLException, ClassNotFoundException;
	
	
	/**
	 * Changes the task status of a specified task to a sepecified value.
	 *
	 * @param id 			the task id
	 * @param status 		the value to which the status should be changed
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 * @throws ClassNotFoundException the class not found exception
	 */
	public boolean updateTaskStatus(int id, String status) throws SQLException, ClassNotFoundException;
}
