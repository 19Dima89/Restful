package de.eridea.rest.types;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class Item.
 *
 * @author Dieter Schneider
 * @since Oct 11, 2015
 */
@XmlRootElement
@XmlType(propOrder={"id", "description"})
public class Item {
	
	/** The Item id. */
	private String id;
	
	/** The Item description. */
	private String description;
	
	/**
	 * Instantiates a new item.
	 *
	 * @param id 			The Item id.
	 * @param description 	The Item description.
	 */
	public Item(String id, String description)
	{
		this.id = id;
		this.description = description;
	}
	
	/**
	 * Gets the Item id.
	 *
	 * @return String - The Item id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the Item description.
	 *
	 * @return String - The Item description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the Item description.
	 *
	 * @param description 	The new Item description.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

}
