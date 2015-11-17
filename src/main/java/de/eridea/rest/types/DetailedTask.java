package de.eridea.rest.types;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"status", "items", "id", "description", "strasse", "plz", "ort", "latitude", "longitude", "type", "information", "hilfsmittel", "auftragsfrist", "eingangsdatum"})
public class DetailedTask extends Task {

	private List<Item> items;
	private String type;
	private String information;
	private String[] hilfsmittel;
	private long[] images;
	private String auftragsfrist;
	private String eingangsdatum;
	
	public DetailedTask(int id, String description, double latitude, double longitude, String status, 
			List<Item> items, int plz, String ort, String strasse, String type, String information, 
			String[] hilfsmittel, long[] images, String auftragsfrist, String eingangsdatum) 
	{
		super(id, description, latitude, longitude, status, strasse, plz, ort);
		this.items = items;
		this.type = type;
		this.information = information;
		this.hilfsmittel = hilfsmittel;
		this.images = images;
		this.auftragsfrist = auftragsfrist;
		this.eingangsdatum = eingangsdatum;
	}

	public long[] getImages() {
		return images;
	}
	
	public String getType() {
		return type;
	}

	public String getInformation() {
		return information;
	}

	public String[] getHilfsmittel() {
		return hilfsmittel;
	}

	public String getAuftragsfrist() {
		return auftragsfrist;
	}

	public String getEingangsdatum() {
		return eingangsdatum;
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public Task returnAsTask()
	{
		return new Task(this.getId(), this.getDescription(), this.getLatitude(), this.getLongitude(), this.getStatus(), this.getStrasse(), this.getPlz(), this.getOrt());
	}
}
