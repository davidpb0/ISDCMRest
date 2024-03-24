package isdcm.isdcmrest.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Video;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.PUT;
import java.io.StringReader;


/**
 *
 * @author 
 */
@Path("jakartaee9")
public class JakartaEE91Resource {
    
    @GET
    public Response ping(){
        return Response
                .ok("ping Jakarta EE")
                .build();
    }
    
    @POST
    @Path("searchVideo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchVideo(String requestBody) {
        // aqui deberia ir el codigo del search video
        return null;
        // aqui deberia ir el codigo del search video
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
}


