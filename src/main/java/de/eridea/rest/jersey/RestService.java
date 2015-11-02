package de.eridea.rest.jersey;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
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
	
	//Declaring variables
	private ArrayList<DetailedTask> dummyData;
	
	@GET
	@Path("/tasks")
	@Produces(MediaType.APPLICATION_JSON)
	public Response responseMsg(@QueryParam("long") Double longitude, @QueryParam("lat") Double latitude,
	@DefaultValue("-1") @QueryParam("maxResults") int maxResults)
	{
		logger.info("Received GET-Request, with the following parameters: longitude="+longitude+", latitude="+latitude
				+", maxResults="+maxResults);
		
		//Initializing dummy data
		initializeDummyData();
		ArrayList<Task> dummyData = convertDetailedTaskToTask(this.dummyData);
		
		if(maxResults <= 0 || maxResults > dummyData.size())
		{
			maxResults = dummyData.size();
		}
		
		logger.info("Response with a list, which consists of "+maxResults+" tasks and the status-code 200");
		return Response.status(200).entity(gson.toJson(generateResponseData(dummyData, maxResults))).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET").build();
	}
	
	@GET
	@Path("/tasks/{parameter}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response responseMsg( @PathParam("parameter") String parameter) {

		logger.info("Received GET-Request to display the details of the task with the ID "+parameter);
		
		DetailedTask output = null;
		
		initializeDummyData();
		
		for(int i=0; i<dummyData.size(); i++)
		{
			if(dummyData.get(i).getId().equals(parameter))
			{
				logger.info("Task with the ID "+parameter+" found among the stored tasks, response with the status-code 200");
				
				output = dummyData.get(i);
				
				return Response.status(200).entity(gson.toJson(output)).header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Methods", "GET").build();
			}
		}
		
		logger.info("Task not found, response with the status-code 404");
		
		return Response.status(404).entity("Task not found!").header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET").build();
	}
	
	private void initializeDummyData()
	{
		dummyData = new ArrayList<DetailedTask>();
		
		dummyData.add(new DetailedTask("1234", "Wartung Maschine 1234", 47.852967, 12.124801, "open", Arrays.asList(new Item("9876", "Ueberspannungsschutz"), new Item("9875", "Generator"))));
		dummyData.add(new DetailedTask("2356", "Wartung Maschine 2356", 47.851192, 12.126987, "open", Arrays.asList(new Item("9876", "Ueberspannungsschutz"), new Item("9875", "Generator"), new Item("9874", "Teileaustausch"))));
		dummyData.add(new DetailedTask("7267", "Wartung Maschine 7267", 47.852042, 12.123618, "open", Arrays.asList(new Item("9876", "Ueberspannungsschutz"))));
		dummyData.add(new DetailedTask("2984", "Wartung Maschine 2984", 47.851848, 12.120646, "open", Arrays.asList(new Item("9875", "Generator"))));
		dummyData.add(new DetailedTask("5112", "Wartung Maschine 5112", 47.853871, 12.116558, "open", Arrays.asList(new Item("9876", "Ueberspannungsschutz"), new Item("9874", "Teileaustausch"))));
		dummyData.add(new DetailedTask("8442", "Wartung Maschine 8442", 47.855289, 12.118264, "open", Arrays.asList(new Item("9874", "Teileaustausch"))));
		dummyData.add(new DetailedTask("8344", "Wartung Maschine 8344", 47.854771, 12.122609, "open", null));
		dummyData.add(new DetailedTask("5691", "Wartung Maschine 5691", 47.852928, 12.119948, "open", Arrays.asList(new Item("9876", "Ueberspannungsschutz"), new Item("9875", "Generator"))));
		dummyData.add(new DetailedTask("7766", "Wartung Maschine 7766", 47.852165, 12.116483, "open", Arrays.asList(new Item("9875", "Generator"))));
		dummyData.add(new DetailedTask("9991", "Wartung Maschine 9991", 47.853490, 12.114681, "open", Arrays.asList(new Item("9876", "Ueberspannungsschutz"), new Item("9875", "Generator"), new Item("9874", "Teileaustausch"))));
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