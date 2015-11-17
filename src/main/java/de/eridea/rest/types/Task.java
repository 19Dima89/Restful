package de.eridea.rest.types;

public class Task {
	
	private int id;
	private String description;
	private double latitude;
	private double longitude;
	
	private String status;
	private String strasse;
	private int plz;
	private String ort;
	
	
	public Task(int id, String description, double latitude, double longitude, String status, String strasse, int plz, String ort)
	{
		this.id = id ;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		
		this.status = status;
		this.strasse = strasse;
		this.plz = plz;
		this.ort = ort;
	}

	public String getStatus() {
		return status;
	}

	public String getStrasse() {
		return strasse;
	}

	public int getPlz() {
		return plz;
	}

	public String getOrt() {
		return ort;
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
