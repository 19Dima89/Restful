package de.eridea.rest.jersey;

import java.util.ArrayList;

public class ResponseObject {
	
	private String id;
	private String description;
	private ArrayList<Location> locations;
	
	public ResponseObject(String id, String description, ArrayList<Location> locations)
	{
		this.id = id;
		this.description = description;
		this.locations = locations;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}
	
}
