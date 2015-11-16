package de.eridea.rest.types;

public class Task {
	
	private int id;
	private String description;
	private double latitude;
	private double longitude;
	
	public Task(int id, String description, double latitude, double longitude)
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

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}
