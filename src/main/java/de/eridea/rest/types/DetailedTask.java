package de.eridea.rest.types;

import java.util.ArrayList;

public class DetailedTask extends Task {

	private String status;
	private String items;
	
	public DetailedTask(String id, String description, double latitude, double longitude, String status, String items) {
		super(id, description, latitude, longitude);
		this.status = status;
		this.items = items;
	}

	public String getStatus() {
		return status;
	}

	public String getItems() {
		return items;
	}
	
	public Task returnAsTask()
	{
		return new Task(this.getId(), this.getDescription(), this.getLatitude(), this.getLongitude());
	}
	

}
