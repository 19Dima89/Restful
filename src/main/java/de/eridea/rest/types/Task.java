package de.eridea.rest.types;


/**
 * The Class Task.
 *
 * @author Dieter Schneider
 * @since Nov 18, 2015
 */
public class Task {
	
	/**  Task ID. */
	private int id;
	
	/**  Task description. */
	private String description;
	
	/** Task latitude. */
	private double latitude;
	
	/** Task longitude. */
	private double longitude;
	
	/** Task status. */
	private String status;
	
	/** Task location (street). */
	private String strasse;
	
	/** Task location (postal code). */
	private int plz;
	
	/** Task location (town). */
	private String ort;
	
	
	/**
	 * Instantiates a new task.
	 *
	 * @param id 		Task ID.
	 * @param description 	Task description.
	 * @param latitude 	Task latitude.
	 * @param longitude 	Task longitude.
	 * @param status 	Task status.
	 * @param strasse 	Task location (street).
	 * @param plz 		Task location (postal code).
	 * @param ort 		Task location (town).
	 */
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

	/**
	 * Gets the status.
	 *
	 * @return String	Status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Gets the location (street).
	 *
	 * @return String	Location (street).
	 */
	public String getStrasse() {
		return strasse;
	}

	/**
	 * Gets the location (postal code).
	 *
	 * @return String	Location (postal code).
	 */
	public int getPlz() {
		return plz;
	}

	/**
	 * Gets the location (town).
	 *
	 * @return String	Location (town).
	 */
	public String getOrt() {
		return ort;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return Double Latitude.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return Double Longitude.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Gets the id.
	 *
	 * @return int	ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the description.
	 *
	 * @return String	Description.
	 */
	public String getDescription() {
		return description;
	}
}
