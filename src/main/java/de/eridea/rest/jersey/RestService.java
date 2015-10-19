package de.eridea.rest.jersey;

import java.util.ArrayList;
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

@Path("/")
public class RestService {
	
	//Initializing the logger
	static final Logger logger = Logger.getRootLogger();
	Gson gson = new GsonBuilder().create();
	
	//Declaring variables
	private ArrayList<Task> dummyData;
	
	@GET
	@Path("/tasks")
	@Produces(MediaType.APPLICATION_JSON)
	public Response responseMsg(@QueryParam("long") Double longitude, @QueryParam("lat") Double latitude,
	@DefaultValue("-1") @QueryParam("maxResults") int maxResults)
	{
		logger.info("Received GET-Request, with the following parameters: longitude="+longitude+", latitude="+latitude
				+", numberOf="+maxResults);
		
		//Initializing dummy data
		initializeDummyData(latitude, longitude);
		
		if(maxResults <= 0 || maxResults > dummyData.size())
		{
			maxResults = dummyData.size();
		}
		
		return Response.status(200).entity(gson.toJson(generateResponseData(dummyData, maxResults))).build();
	}
	
	@GET
	@Path("/tasks/{parameter}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response responseMsg( @PathParam("parameter") String parameter,
			@DefaultValue("Nothing to say") @QueryParam("value") String value) {

		String output = "Hello from: " + parameter + " : " + value;

		return Response.status(200).entity(output).build();
	}
	
	private void initializeDummyData(double latitude, double longitude)
	{
		dummyData = new ArrayList<Task>();
		
		dummyData.add(new Task("1234", "Wartung Maschine 1234", latitude, longitude));
		dummyData.add(new Task("2356", "Wartung Maschine 2356", 47.851192, 12.126987));
		dummyData.add(new Task("7267", "Wartung Maschine 7267", 47.852042, 12.123618));
		dummyData.add(new Task("2984", "Wartung Maschine 2984", 47.851848, 12.120646));
		dummyData.add(new Task("5112", "Wartung Maschine 5112", 47.853871, 12.116558));
		dummyData.add(new Task("8442", "Wartung Maschine 8442", 47.855289, 12.118264));
		dummyData.add(new Task("8344", "Wartung Maschine 8344", 47.854771, 12.122609));
		dummyData.add(new Task("5691", "Wartung Maschine 5691", 47.852928, 12.119948));
		dummyData.add(new Task("7766", "Wartung Maschine 7766", 47.852165, 12.116483));
		dummyData.add(new Task("9991", "Wartung Maschine 9991", 47.853490, 12.114681));
			
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