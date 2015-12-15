package de.eridea.rest.jersey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
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
	//public static final String imageDirectory = "/opt/tomcat/webapps/images/";
	public static final String imageDirectory = "/opt/apache-tomcat-7.0.65/webapps/images/";
	
	/** Document directory of the server. */
	//public static final String documentDirectory = "/opt/tomcat/webapps/documents/";
	public static final String documentDirectory = "/opt/apache-tomcat-7.0.65/webapps/documents/";
	
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
			ArrayList<Task> convertedTasks = convertDetailedTaskToTask(retrievedTasks);
			
			if(maxResults <= 0 || maxResults > convertedTasks.size())
			{
				maxResults = convertedTasks.size();
			}
			
			logger.info("Response with a list, which consists of "+maxResults+" tasks and the status-code 200");
			
			return Response.status(200).entity(gson.toJson(generateResponseData(convertedTasks, maxResults)))
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
			output.setDocuments(getDocumentList(id, documentDirectory));
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
	 * @param id 		the image id
	 * @return HTTP Response for an image download.
	 */
	@GET
	@Path("/tasks/{task_id}/images/{img_id}")
	@Produces("image/png")
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
	 * @return HTTP Response for the image upload.
	 */
	@POST
	@Path("/tasks/{id}/images")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadImage(
		@FormDataParam("img") InputStream uploadedInputStream,
		@FormDataParam("img") FormDataContentDisposition fileDetail,
		@PathParam("id") int id) {
		
		logger.info("Received POST-Request to upload a file with the name "+fileDetail.getFileName());
		
		String extension = getFormat(fileDetail.getFileName());
		
		boolean uploadSuccessful = false;
		
		if(extension != null && !extension.equals("PDF"))
		{
			Calendar calendar = Calendar.getInstance();
			long imgID = calendar.getTimeInMillis();
			
			String uploadedFileLocation = imageDirectory + id+"_"+imgID+"."+extension.toLowerCase();
	
			uploadSuccessful = writeToFile(uploadedInputStream, uploadedFileLocation);
		}
		
		if(uploadSuccessful)
		{
			logger.info("File "+fileDetail.getFileName()+" uploaded successfully!");
			
			String output = "File uploaded!";
	
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
	 * @param name 		the name of the document
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
	 * @return HTTP Response for the document upload.
	 */
	@POST
	@Path("/tasks/{id}/documents")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadDoc(
			@FormDataParam("doc") InputStream uploadedInputStream,
			@FormDataParam("doc") FormDataContentDisposition fileDetail,
			@PathParam("id") int id)
	{
		logger.info("Received POST-Request to upload a document with the name "+fileDetail.getFileName());
		
		String extension = getFormat(fileDetail.getFileName());
		
		boolean uploadSuccessful = false;
		
		if(extension.equals("PDF"))
		{
			Calendar calendar = Calendar.getInstance();
			long docID = calendar.getTimeInMillis();
			
			String uploadedFileLocation = documentDirectory + id+"_"+docID+".pdf";
	
			uploadSuccessful = writeToFile(uploadedInputStream, uploadedFileLocation);
		}
		
		if(uploadSuccessful)
		{
			logger.info("File "+fileDetail.getFileName()+" uploaded successfully!");
			
			String output = "File uploaded!";
	
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
	 * Save a file to the directory specified in @param uploadedFileLocation.
	 *
	 * @param uploadedInputStream the RestService uploaded input stream
	 * @param uploadedFileLocation the RestService uploaded file location
	 * @return true, if successful
	 */
	private boolean writeToFile(InputStream uploadedInputStream,
		String uploadedFileLocation) {

		try 
		{
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			
			return true;
		} 
		catch (IOException e) 
		{
			return false;
		}
	}
	
	/**
	 * Converts an ArrayList of DetailedTask to an ArrayList of simple Tasks.
	 *
	 * @param dTask 		ArrayList, which should be converted
	 * @return ArrayList<Task> 	Converted List of Tasks
	 */
	private ArrayList<Task> convertDetailedTaskToTask(ArrayList<DetailedTask> dTask)
	{
		ArrayList<Task> returnList = new ArrayList<Task>();
		
		for(int i=0;i<dTask.size(); i++)
		{
			returnList.add(dTask.get(i).returnAsTask());
		}
		
		return returnList;
	}
	
	/**
	 * Generates an ArrayList of Tasks, which is limited to the number of maxResults.
	 *
	 * @param inputList 		The input list
	 * @param maxResults 		Max number of entries the list should have
	 * @return ArrayList<Task>	Generated limited list
	 */
	private ArrayList<Task> generateResponseData(ArrayList<Task> inputList, int maxResults)
	{
		ArrayList<Task> temp = new ArrayList<Task>();
		
		for(int i=0; i<maxResults; i++)
		{
			temp.add(inputList.get(i));
		}
		
		return temp;
	}
	
	public String getFormat(String imageName)
	{
	    String temp = new String(imageName);
	    temp.toLowerCase();

	    if(temp.endsWith(".png"))
	    {
	    	return "PNG";
	    }
	    else if(temp.endsWith(".jpg") || temp.endsWith("jpeg"))
	    {
	    	return "JPEG";
	    }
	    else if(temp.endsWith(".pdf"))
	    {
	    	return "PDF";
	    }
	    else
	    {
	    	return null;
	    }
	        
	}
	
	private List<String> getDocumentList(int taskID, String path)
	{
		ArrayList<String> returnValue = new ArrayList<String>();
		
		File[] listOfFiles = new File(path).listFiles();
		
		for(int i=0;i<listOfFiles.length;i++)
		{
			String[] explodedName = listOfFiles[i].getName().split("_");
			
			if(explodedName[0].equals(Integer.toString(taskID)))
			{
				returnValue.add(explodedName[1].substring(0, explodedName[1].length()-4));
			}
		}
		
		if(returnValue.isEmpty())
		{
			return null;
		}
		
		return returnValue;
	}
	
	@GET
	@Path("/debugging/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response debuggingGET(@PathParam("id") int id)
	{
		
			StringBuilder sb = new StringBuilder();
			List<String> res = getDocumentList(id, documentDirectory);
			
			for(int i=0;i<res.size(); i++)
			{
				sb.append(res.get(i)+"\n");
			}
		
			return Response.status(200).entity(gson.toJson(sb.toString())).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").build();
		
	}
}
