package de.eridea.rest.jersey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.eridea.rest.types.DetailedTask;
import de.eridea.rest.types.Task;

public class UtilityClass {
	
	private UtilityClass()
	{
		
	}
	
	/**
	 * Gets a list of all the files in the specified directory.
	 *
	 * @param taskID 		task id, which files should be found.
	 * @param path 			directory path which s
	 * @return a list which contains Strings with absolute file paths. 
	 */
	public static List<String> getFileList(int taskID, String path)
	{
		ArrayList<String> returnValue = new ArrayList<String>();
		
		File[] listOfFiles = new File(path).listFiles();
		
		for(int i=0;i<listOfFiles.length;i++)
		{
			String[] explodedName = listOfFiles[i].getName().split("_");
			
			if(explodedName[0].equals(Integer.toString(taskID)))
			{
				returnValue.add(explodedName[1].substring(0, explodedName[1].length()-4));
			}
		}
		
		if(returnValue.isEmpty())
		{
			return null;
		}
		
		return returnValue;
	}
	
	/**
	 * Gets the format of the given file (according to its name).
	 *
	 * @param imageName the RestService image name
	 * @return the format
	 */
	public static String getFileFormat(String imageName)
	{
	    String temp = new String(imageName);
	    temp.toLowerCase();

	    if(temp.endsWith(".png"))
	    {
	    	return "PNG";
	    }
	    else if(temp.endsWith(".jpg") || temp.endsWith("jpeg"))
	    {
	    	return "JPG";
	    }
	    else if(temp.endsWith(".pdf"))
	    {
	    	return "PDF";
	    }
	    else
	    {
	    	return null;
	    }
	}
	
	/**
	 * Generates an ArrayList of Tasks, which is limited to the number of maxResults.
	 *
	 * @param inputList 		The input list
	 * @param maxResults 		Max number of entries the list should have
	 * @return ArrayList<Task>	Generated limited list
	 */
	public static ArrayList<Task> generateResponseData(ArrayList<Task> inputList, int maxResults)
	{
		ArrayList<Task> temp = new ArrayList<Task>();
		
		for(int i=0; i<maxResults; i++)
		{
			temp.add(inputList.get(i));
		}
		
		return temp;
	}
	
	/**
	 * Converts an ArrayList of DetailedTask to an ArrayList of simple Tasks.
	 *
	 * @param dTask 		ArrayList, which should be converted
	 * @return ArrayList<Task> 	Converted List of Tasks
	 */
	public static ArrayList<Task> convertDetailedTaskToTask(ArrayList<DetailedTask> dTask)
	{
		ArrayList<Task> returnList = new ArrayList<Task>();
		
		for(int i=0;i<dTask.size(); i++)
		{
			returnList.add(dTask.get(i).returnAsTask());
		}
		
		return returnList;
	}
	
	/**
	 * Save a file to the directory specified in @param uploadedFileLocation.
	 *
	 * @param uploadedInputStream the RestService uploaded input stream
	 * @param uploadedFileLocation the RestService uploaded file location
	 * @return true, if successful
	 */
	public static boolean writeToFile(InputStream uploadedInputStream,
		String uploadedFileLocation) 
	{
		try 
		{
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			
			return true;
		} 
		catch (IOException e) 
		{
			return false;
		}
	}

}
