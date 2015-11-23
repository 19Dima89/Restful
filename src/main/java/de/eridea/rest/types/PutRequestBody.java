package de.eridea.rest.types;

public class PutRequestBody {
	
	private String status = null;
	
	public PutRequestBody(String status)
	{
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
