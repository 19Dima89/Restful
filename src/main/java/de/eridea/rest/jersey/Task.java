package de.eridea.rest.jersey;

public class Task {
	
	private String id;
	private String description;
	private double latitude;
	private double longitude;
	
	public Task(String id, String description, double latitude, double longitude)
	{
		this.id = id ;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}
