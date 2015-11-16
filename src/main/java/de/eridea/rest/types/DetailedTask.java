package de.eridea.rest.types;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"status", "items", "id", "description", "strasse", "plz", "ort", "latitude", "longitude", "type", "information", "hilfsmittel", "auftragsfrist", "eingangsdatum"})
public class DetailedTask extends Task {

	private String status;
	private List<Item> items;
	private int plz;
	private String ort;
	private String strasse;
	private String type;
	private String information;
	private String[] hilfsmittel;
	private long[] images;
	private long auftragsfrist;
	private long eingangsdatum;
	
	public DetailedTask(int id, String description, double latitude, double longitude, String status, 
			List<Item> items, int plz, String ort, String strasse, String type, String information, 
			String[] hilfsmittel, long[] images, long auftragsfrist, long eingangsdatum) 
	{
		super(id, description, latitude, longitude);
		this.status = status;
		this.items = items;
		this.plz = plz;
		this.ort = ort;
		this.strasse = strasse;
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

	public int getPlz() {
		return plz;
	}

	public String getOrt() {
		return ort;
	}

	public String getStrasse() {
		return strasse;
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

	public long getAuftragsfrist() {
		return auftragsfrist;
	}

	public long getEingangsdatum() {
		return eingangsdatum;
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
