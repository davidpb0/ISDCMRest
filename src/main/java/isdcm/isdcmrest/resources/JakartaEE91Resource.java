package isdcm.isdcmrest.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Video;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.ws.rs.PUT;
import java.io.StringReader;
import java.util.List;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.Produces;
import model.User;


/**
 *
 * @author 
 */
@Path("jakartaee9")
public class JakartaEE91Resource {
    private static final String SECRET_KEY = "e2c8d7d7-31b2-4a9f-a4dc-0a7e6a4f73b9";
    
    @GET
    public Response ping(){
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    

    @POST
    @Path("searchVideo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchVideo(String requestBody) {
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(requestBody));

            JsonObject jsonObject = jsonReader.readObject();

            jsonReader.close();

            String filter = jsonObject.getString("filter");
            String value = jsonObject.getString("value");
            
            System.out.println("Entro bien" + filter + " " + value);

            List<Video> videoList = Video.getVideosByFilter(filter, value);

            // Create a JSON array builder to hold video JSON objects
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

            // Convert each Video object to JSON and add to the array builder
            for (Video video : videoList) {
                JsonObjectBuilder videoBuilder = Json.createObjectBuilder()
                        .add("id", video.getId())
                        .add("title", video.getTitle())
                        .add("author", video.getAuthor())
                        .add("creationDate", video.getCreationDate())
                        .add("duration", video.getDuration())
                        .add("reproductions", video.getReproductions())
                        .add("description", video.getDescription())
                        .add("format", video.getFormat())
                        .add("path", video.getPath());
                jsonArrayBuilder.add(videoBuilder.build());
            }

            // Build the JSON array
            JsonArray jsonArray = jsonArrayBuilder.build();

            // Respond with the JSON array
            return Response.ok(jsonArray.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Error processing search request").build();
        }
    }
    
    @PUT
    @Path("updateReproductions")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReproductions(String requestBody) {
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(requestBody));

            JsonObject jsonObject = jsonReader.readObject();

            jsonReader.close();

            String title = jsonObject.getString("title");
            String author = jsonObject.getString("author");

            Video.updateReproductions(title, author);

            String responseMessage = "Reproductions updated for video: " + title;

            return Response.ok(responseMessage).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Failed to update reproductions").build();
        }
    }
    
    /**
    * POST method to login in the application
    * @param username
    * @param password
    * @return
    */
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Login(@FormParam("username") String username,
    @FormParam("password") String password){

         System.out.println(username);
         System.out.println(password);
         
        if (User.authenticateUser(username, password)) {
            String token = Jwts.builder()
                   .setSubject(username)
                   .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                   .compact();
            // Devuelve el token como respuesta
            return Response.ok().entity("{\"token\": \"" + token + "\"}").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                       .entity("{\"error\": \"Authentication failed\"}")
                       .build();
  
    }
}


