package de.eridea.rest.types;

import java.util.List;

public class DetailedTask extends Task {

	private String status;
	private List<Item> items;
	
	public DetailedTask(String id, String description, double latitude, double longitude, String status, List<Item> items) {
		super(id, description, latitude, longitude);
		this.status = status;
		this.items = items;
	}

	public String getStatus() {
		return status;
	}

	public List<Item> getItems() {
		return items;
	}
	
	public Task returnAsTask()
	{
		return new Task(this.getId(), this.getDescription(), this.getLatitude(), this.getLongitude());
	}

}
