package de.eridea.rest.jersey;

public class Location {
	
	private double latitude;
	private double longitude;
	private double superTest;
	
	public Location(double latitude, double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
