package de.eridea.rest.jersey;

import java.util.ArrayList;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/service")
public class RestService {
	
	//Declaring variables
	ArrayList<Location> dummyLocations;
	ResponseObject respObj;
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseObject responseMsg(@QueryParam("long") Double longitude, @QueryParam("lat") Double latitude,
	@DefaultValue("3") @QueryParam("numberOf") int numberOf)
	{
		if(numberOf <= 0)
		{
			numberOf = 3;
		}
		
		//Initializing dummy data
		dummyLocations = new ArrayList<Location>();
		
		//Fetching requested number of locations
		for(int i=0; i<numberOf; i++)
		{
			dummyLocations.add(new Location(47.857480+i, 12.119870+i));
		}
		
		//Creating new ResponseObject
		respObj = new ResponseObject("A-17002", "Sanitär Auftrag", dummyLocations);
		
		return respObj;
	}
}