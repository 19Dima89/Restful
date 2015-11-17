package de.eridea.rest.jersey;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.eridea.rest.types.DetailedTask;
import de.eridea.rest.types.Item;
import de.eridea.rest.types.Task;

@Path("/")
public class RestService {
	
	//Initializing the logger
	static final Logger logger = Logger.getRootLogger();
	private Gson gson = new GsonBuilder().create();
	
	@GET
	@Path("/tasks")
	@Produces(MediaType.APPLICATION_JSON)
	public Response responseMsg(@QueryParam("long") Double longitude, @QueryParam("lat") Double latitude,
	@DefaultValue("-1") @QueryParam("maxResults") int maxResults)
	{
		logger.info("Received GET-Request, with the following parameters: longitude="+longitude+", latitude="+latitude
				+", maxResults="+maxResults);
		
		ArrayList<Task> convertedTasks = convertDetailedTaskToTask(JDBConnection.selectAllTasks());
		
		if(maxResults <= 0 || maxResults > convertedTasks.size())
		{
			maxResults = convertedTasks.size();
		}
		
		logger.info("Response with a list, which consists of "+maxResults+" tasks and the status-code 200");
		return Response.status(200).entity(gson.toJson(generateResponseData(convertedTasks, maxResults))).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET").build();
	}
	
	@GET
	@Path("/tasks/{parameter}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response responseMsg( @PathParam("parameter") int parameter) {

		logger.info("Received GET-Request to display the details of the task with the ID "+parameter);
		
		DetailedTask output = null;
		
		DetailedTask dt = new DetailedTask(1239, "Wartung Maschine 1234", 47.852967, 12.124801, 
		 		"open", Arrays.asList(new Item("9876", "Ueberspannungsschutz"), 
				new Item("9875", "Generator")), 83022, "Rosenheim", "Hochschulestr. 1", 
		 		"Wartung", "1. Stock", new String[]{"hammer", "bohrmaschine"}, 
		 		new long[]{123, 456}, "12.12.2015", "12.10.2015");
				
		JDBConnection.insertTask(dt);
		
		output = JDBConnection.selectTask(parameter);
		
		return Response.status(200).entity(gson.toJson(output)).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET").build();
		
		
		//logger.info("Task not found, response with the status-code 404");
		
		/*return Response.status(404).entity("Task not found!").header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET").build();*/
	}
	
	private ArrayList<Task> convertDetailedTaskToTask(ArrayList<DetailedTask> dTask)
	{
		ArrayList<Task> returnList = new ArrayList<Task>();
		
		for(int i=0;i<dTask.size(); i++)
		{
			returnList.add(dTask.get(i).returnAsTask());
		}
		
		return returnList;
	}
	
	private ArrayList<Task> generateResponseData(ArrayList<Task> dummyData, int maxResults)
	{
		ArrayList<Task> temp = new ArrayList<Task>();
		
		for(int i=0; i<maxResults; i++)
		{
			temp.add(dummyData.get(i));
		}
		
		return temp;
	}
}