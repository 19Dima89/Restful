package de.eridea.rest.types;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class DetailedTask.
 *
 * @author Dieter Schneider
 * @since Nov 18, 2015
 */
@XmlRootElement
@XmlType(propOrder={"status", "items", "id", "description", "strasse", "plz", "ort", "latitude", "longitude", "type", "information", "hilfsmittel", "auftragsfrist", "eingangsdatum"})
public class DetailedTask extends Task {

	/** The DetailedTask items. */
	private List<Item> items;
	
	/** The DetailedTask type. */
	private String type;
	
	/** The DetailedTask information. */
	private String information;
	
	/** The DetailedTask hilfsmittel. */
	private String[] hilfsmittel;
	
	/** The DetailedTask images. */
	private List<String> images;
	
	/** The DetailedTask auftragsfrist. */
	private String auftragsfrist;
	
	/** The DetailedTask eingangsdatum. */
	private String eingangsdatum;
	
	private List<String> documents = null;
	
	/**
	 * Instantiates a new detailed task.
	 *
	 * @param id 			The DetailedTask id.
	 * @param description 		The DetailedTask description.
	 * @param latitude 		The DetailedTask latitude.
	 * @param longitude 		The DetailedTask longitude.
	 * @param status 		The DetailedTask status.
	 * @param items 		The DetailedTask items.
	 * @param plz 			The DetailedTask plz.
	 * @param ort 			The DetailedTask ort.
	 * @param strasse 		The DetailedTask strasse.
	 * @param type 			The DetailedTask type.
	 * @param information 		The DetailedTask information.
	 * @param hilfsmittel 		The DetailedTask hilfsmittel.
	 * @param images 		The DetailedTask images.
	 * @param auftragsfrist 	The DetailedTask auftragsfrist.
	 * @param eingangsdatum 	The DetailedTask eingangsdatum.
	 */
	public DetailedTask(int id, String description, double latitude, double longitude, String status, 
			List<Item> items, int plz, String ort, String strasse, String type, String information, 
			String[] hilfsmittel, String auftragsfrist, String eingangsdatum) 
	{
		super(id, description, latitude, longitude, status, strasse, plz, ort);
		this.items = items;
		this.type = type;
		this.information = information;
		this.hilfsmittel = hilfsmittel;
		this.auftragsfrist = auftragsfrist;
		this.eingangsdatum = eingangsdatum;
	}

	/**
	 * Gets the DetailedTask images.
	 *
	 * @return long[] - The DetailedTask images.
	 */
	public List<String> getImages() {
		return images;
	}
	
	
	public void setImages(List<String> images) {
		this.images = images;
	}

	/**
	 * Gets the DetailedTask type.
	 *
	 * @return String - The DetailedTask type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the DetailedTask information.
	 *
	 * @return String - The DetailedTask information.
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * Gets the DetailedTask hilfsmittel.
	 *
	 * @return String[] - The DetailedTask hilfsmittel.
	 */
	public String[] getHilfsmittel() {
		return hilfsmittel;
	}

	/**
	 * Gets the DetailedTask auftragsfrist.
	 *
	 * @return String - The DetailedTask auftragsfrist.
	 */
	public String getAuftragsfrist() {
		return auftragsfrist;
	}

	/**
	 * Gets the DetailedTask eingangsdatum.
	 *
	 * @return String - The DetailedTask eingangsdatum.
	 */
	public String getEingangsdatum() {
		return eingangsdatum;
	}
	
	/**
	 * Gets the DetailedTask items.
	 *
	 * @return List<Item> - The DetailedTask items.
	 */
	public List<Item> getItems() {
		return items;
	}
	
	public List<String> getDocuments() {
		return documents;
	}

	public void setDocuments(List<String> documents) {
		this.documents = documents;
	}

	/**
	 * Converts the DetaildTask to a simple Task.
	 *
	 * @return DetailedTask - The converted task.
	 */
	public Task returnAsTask()
	{
		return new Task(this.getId(), this.getDescription(), this.getLatitude(), this.getLongitude(), this.getStatus(), this.getStrasse(), this.getPlz(), this.getOrt());
	}
}
