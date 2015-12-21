package de.eridea.rest.jersey;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import de.eridea.rest.database.DBConnection;
import de.eridea.rest.database.JDBConnection;
import de.eridea.rest.types.DetailedTask;
import de.eridea.rest.types.PutRequestBody;
import de.eridea.rest.types.Task;

/**
 * Class that Responds to GET and POST Requests.
 * 
 * @author 	Dieter Schneider
 * @since	11.10.2015
 */
@Path("/")
public class RestService {
	
	/** Logger instance. */
	private static final Logger logger = Logger.getRootLogger();
	
	/** Image directory of the server. */
	public static final String imageDirectory = "/opt/tomcat/webapps/images/";
	//public static final String imageDirectory = "/opt/apache-tomcat-7.0.65/webapps/images/";
	
	/** Document directory of the server. */
	public static final String documentDirectory = "/opt/tomcat/webapps/documents/";
	//public static final String documentDirectory = "/opt/apache-tomcat-7.0.65/webapps/documents/";
	
	/** Gson builder. */
	private Gson gson = new GsonBuilder().create();
	
	/** Database interaction. */
	private DBConnection dbInterface = new JDBConnection();
	//private DBConnection dbInterface = new HardcodedData();
	
	/**
	 * Responds to a GET-Request, by listing all available tasks.
	 *
	 * @param longitude 	longitude of a task
	 * @param latitude 		latitude of a task
	 * @param maxResults 	max number of results inside the response
	 * @return Response 	HTTP Response, which contains all tasks (limited by maxResults) and the status-code 200 (if task found)
	 * 						or the status-code 404 (if task is not found)	
	 */
	@GET
	@Path("/tasks")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllTasks(@QueryParam("long") Double longitude, @QueryParam("lat") Double latitude,
	@DefaultValue("-1") @QueryParam("maxResults") int maxResults)
	{
		logger.info("Received GET-Request, with the following parameters: longitude="+longitude+", latitude="+latitude
				+", maxResults="+maxResults);	
		
		ArrayList<DetailedTask> retrievedTasks = null;
		
		try 
		{
			retrievedTasks = dbInterface.selectAllTasks();
		} 
		catch (SQLException e) 
		{
			logger.info("Could not establish a connection to the Database. Authentification? (selectAllTasks)");
	    	 
	         e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			logger.info("org.postgresql.Driver Class not found. (selectAllTasks)");
	    	  
	    	 e.printStackTrace();
		}
		
		
		if(retrievedTasks.isEmpty() || retrievedTasks == null)
		{
			logger.info("No tasks found response with the status-code 404");
			
			return Response.status(404).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").build();
		}
		else
		{
			ArrayList<Task> convertedTasks = UtilityClass.convertDetailedTaskToTask(retrievedTasks);
			
			if(maxResults <= 0 || maxResults > convertedTasks.size())
			{
				maxResults = convertedTasks.size();
			}
			
			logger.info("Response with a list, which consists of "+maxResults+" tasks and the status-code 200");
			
			return Response.status(200).entity(gson.toJson(UtilityClass.generateResponseData(convertedTasks, maxResults)))
					.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET").build();
		}
	}
	
	/**
	 * Responds to a GET-Request, by showing the requested Task.
	 *
	 * @param id 		ID of a task that should be displayed
	 * @return Response	HTTP Response, which contains the specified task and the status-code 200 (if task found)
	 * 			or the status-code 404 (if task is not found)
	 */
	@GET
	@Path("/tasks/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showTask( @PathParam("id") int id) {

		logger.info("Received GET-Request to display the details of the task with the ID "+id);
		
		DetailedTask output = null;
		
		try 
		{
			output = dbInterface.selectTask(id);
			output.setDocuments(UtilityClass.getFileList(id, documentDirectory));
			output.setImages(UtilityClass.getFileList(id, imageDirectory));
		} 
		catch (SQLException e) 
		{
			logger.info("Could not establish a connection to the Database. Authentification? (selectTask)");
	    	 
	         e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			logger.info("org.postgresql.Driver Class not found. (selectTask)");
	    	  
	    	 e.printStackTrace();
		}
		
		if(output != null)
		{
			logger.info("Task "+id+" found, response with the staus-code 200!");
			
			return Response.status(200).entity(gson.toJson(output)).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET").build();
		}
		else
		{
			logger.info("Task "+id+" not found, response with the status-code 404!");
			
			return Response.status(404).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").build();
		}
	}
	
	/**
	 * Changes the status of the task with the specified ID to a specified value.
	 *
	 * @param requestBody 		the PUT-Request body
	 * @param id 				the id of the task whos status should be changed 
	 * @return HTTP response (statuscode 200, if status changed otherwise statuscode 400)
	 */
	@PUT
	@Path("/tasks/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putStatus(String requestBody, @PathParam("id") int id)
	{
		logger.info("Received PUT-Request to change the status of task "+id);
		
		PutRequestBody prb = null;
		
		try
		{
			prb = gson.fromJson(requestBody, PutRequestBody.class);
		}
		catch(Exception e)
		{
			logger.info("Could not convert request body json to prb");
		}
		
		if(prb == null)
		{
			logger.info("Wrong request body -> Response status 400 (putStatus)");
			
			return Response.status(400).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "PUT").build();
		}
		
		boolean statusChanged = false;
		
		try
		{
			statusChanged = dbInterface.updateTaskStatus(id, prb.getStatus());
		}
		catch(SQLException e)
		{
			logger.info("Could not establish a connection to the Database. Authentification? (putStatus)");
			
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			logger.info("org.postgresql.Driver Class not found. (putStatus)");
			
			e.printStackTrace();
		}
		catch(Exception e)
		{
			logger.info("General exception (putStatus)");
			
			e.printStackTrace();
		}
		
		if(statusChanged)
		{
			logger.info("Status of task "+id+" changed to "+prb.getStatus()+" -> Response Status 200 (putStatus)");
			
			return Response.status(200).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "PUT").build();
		}
		else
		{
			logger.info("Status of task "+id+" does not need to be changed, because it is already on "+prb.getStatus()
					+ " -> Response status 400 (putStatus");
			
			return Response.status(400).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "PUT").build();
		}
	}
	
	/**
	 * Gets a single image from the server based on its name.
	 *
	 * @param task_id the RestService task_id
	 * @param img_id the RestService img_id
	 * @return HTTP Response for an image download.
	 */
	@GET
	@Path("/tasks/{task_id}/images/{img_id}")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response getImage(@PathParam("task_id") int task_id, @PathParam("img_id") long img_id)
	{
		logger.info("Received GET-Request to download the file with the name "+task_id+"_"+img_id);
		
		File responseEntity = dbInterface.getImage(task_id+"_"+img_id);
		
		if(responseEntity != null)
		{
			logger.info("File "+task_id+"_"+img_id+" exists on this server! Response with the status-code 200 and the attached file.");
			
			return Response.ok((Object) responseEntity).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").build();
		}
		else
		{
			logger.info("File "+task_id+"_"+img_id+" not found on this server! Response with the status-code 400.");
			
			return Response.status(404).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").build();
		}
	}
	
	/**
	 * Upload an image to the servers images directory.
	 *
	 * @param uploadedInputStream 		input stream of the uploaded image.
	 * @param fileDetail 				meta-data of the uploaded image.
	 * @param id the RestService id
	 * @return HTTP Response for the image upload.
	 */
	@POST
	@Path("/tasks/{id}/images")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadImage(
		@FormDataParam("img") InputStream uploadedInputStream,
		@FormDataParam("img") FormDataContentDisposition fileDetail,
		@PathParam("id") int id) {
		
		logger.info("Received POST-Request to upload a file with the name "+fileDetail.getFileName());
		
		String extension = UtilityClass.getFileFormat(fileDetail.getFileName());
		
		boolean uploadSuccessful = false;
		long imgID = 0;
		
		if(extension != null && !extension.equals("PDF"))
		{
			Calendar calendar = Calendar.getInstance();
			imgID = calendar.getTimeInMillis();
			
			String uploadedFileLocation = imageDirectory + id+"_"+imgID+"."+extension.toLowerCase();
	
			uploadSuccessful = UtilityClass.writeToFile(uploadedInputStream, uploadedFileLocation);
		}
		
		if(uploadSuccessful && imgID != 0)
		{
			logger.info("File "+fileDetail.getFileName()+" uploaded successfully! Name changed to "+id+"_"+imgID);
			
			String output = Long.toString(imgID);
	
			return Response.status(200).entity(output).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "POST").build();
		}
		else
		{
			logger.info("File "+fileDetail.getFileName()+" not uploaded due to an IOException!");
			
			String output = "File upload failed!";
			
			return Response.status(500).entity(output).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "POST").build();
		}
	}
	
	/**
	 * Gets a single document from the server based on its name.
	 *
	 * @param task_id the RestService task_id
	 * @param doc_id the RestService doc_id
	 * @return HTTP Response for a document download.
	 */
	@GET
	@Path("/tasks/{task_id}/documents/{doc_id}")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response getDocument(@PathParam("task_id") int task_id, @PathParam("doc_id") long doc_id)
	{
		logger.info("Received GET-Request to download the file with the name "+task_id+"_"+doc_id);
		
		File responseEntity = dbInterface.getDocument(task_id+"_"+doc_id);
		
		if(responseEntity != null)
		{
			logger.info("File "+task_id+"_"+doc_id+" exists on this server! Response with the status-code 200 and the attached file.");
			
			return Response.ok((Object) responseEntity).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").build();
		}
		else
		{
			logger.info("File "+task_id+"_"+doc_id+" not found on this server! Response with the status-code 400.");
			
			return Response.status(404).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").build();
		}
	}
	
	/**
	 * Upload a single document to the servers document directory.
	 *
	 * @param uploadedInputStream 		input stream of the uploaded document.
	 * @param fileDetail 				meta-data of the uploaded document.
	 * @param id the RestService id
	 * @return HTTP Response for the document upload.
	 */
	@POST
	@Path("/tasks/{id}/documents")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadDoc(
			@FormDataParam("doc") InputStream uploadedInputStream,
			@FormDataParam("doc") FormDataContentDisposition fileDetail,
			@PathParam("id") int id)
	{
		logger.info("Received POST-Request to upload a document with the name "+fileDetail.getFileName());
		
		String extension = UtilityClass.getFileFormat(fileDetail.getFileName());
		
		boolean uploadSuccessful = false;
		long docID = 0;
		
		if(extension.equals("PDF"))
		{
			Calendar calendar = Calendar.getInstance();
			docID = calendar.getTimeInMillis();
			
			String uploadedFileLocation = documentDirectory + id+"_"+docID+".pdf";
	
			uploadSuccessful = UtilityClass.writeToFile(uploadedInputStream, uploadedFileLocation);
		}
		
		if(uploadSuccessful && docID != 0)
		{
			logger.info("File "+fileDetail.getFileName()+" uploaded successfully! Name changed to "+id+"_"+docID);
			
			String output = Long.toString(docID);
	
			return Response.status(200).entity(output).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "POST").build();
		}
		else
		{
			logger.info("File "+fileDetail.getFileName()+" not uploaded due to an IOException!");
			
			String output = "File upload failed!";
			
			return Response.status(500).entity(output).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "POST").build();
		}
	}
	
	/**
	 * Delete a specified image on the server.
	 *
	 * @param task_id 		the id of the task linked to the image.
	 * @param img_id 		the id of the image.
	 * @return HTTP Response for the image delete.
	 */
	@DELETE
	@Path("tasks/{task_id}/images/{img_id}")
	public Response deleteImage(@PathParam("task_id") int task_id, @PathParam("img_id") long img_id)
	{
		logger.info("Received order to delete the image with the name "+task_id +"_"+img_id);
		
		File fileToDelete = null;
		
		if(new File(imageDirectory + task_id + "_" + img_id + ".png").exists())
		{
			fileToDelete = new File(imageDirectory + task_id + "_" + img_id + ".png");
		}
		else if(new File(imageDirectory + task_id + "_" + img_id + ".jpg").exists())
		{
			fileToDelete = new File(imageDirectory + task_id + "_" + img_id + ".jpg");
		}
		
		if(fileToDelete != null)
		{
			fileToDelete.delete();
			
			logger.info("Image with the name "+task_id +"_"+img_id+" deleted! Response with status-code 200");
			
			return Response.status(200).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "DELETE").build();
		}
		else
		{
			logger.info("Image "+task_id +"_"+img_id+" could not be deleted, because it could not "
					+ "be found on the server! Response with the status-code 400");
			
			return Response.status(400).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "DELETE").build();
		}
	}
	
	/**
	 * Delete a specified document on the server.
	 *
	 * @param task_id 		the id of the task linked to the document.
	 * @param doc_id 		the id of the document.
	 * @return HTTP Response for the document delete.
	 */
	@DELETE
	@Path("tasks/{task_id}/documents/{doc_id}")
	public Response deleteDocument(@PathParam("task_id") int task_id, @PathParam("doc_id") long doc_id)
	{
		logger.info("Received order to delete the document with the name "+task_id +"_"+doc_id);
		
		File fileToDelete = null;
		
		if(new File(documentDirectory + task_id + "_" + doc_id + ".pdf").exists())
		{
			fileToDelete = new File(documentDirectory + task_id + "_" + doc_id + ".pdf");
		}
		
		if(fileToDelete != null)
		{
			fileToDelete.delete();
			logger.info("Document with the name "+task_id +"_"+doc_id+" deleted! Response with status-code 200");
			
			return Response.status(200).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "DELETE").build();
		}
		else
		{
			logger.info("Document "+task_id +"_"+doc_id+" could not be deleted, because it could not "
					+ "be found on the server! Response with the status-code 400");
			
			return Response.status(400).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "DELETE").build();
		}
	}
}
