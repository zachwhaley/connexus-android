package ginger.connexus.network;

import ginger.connexus.model.ConnexusImage;
import ginger.connexus.model.ConnexusStream;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Connexus REST API as Java Interface
 * 
 * @author zwhaley
 */
public interface ConnexusApi {

    @GET("/AllStreamsServletAPI")
    ConnexusStream.List allStreams();

    // TEMP
    @GET("/AllStreamsServletAPI")
    ConnexusStream.List subscribedStreams();

    // TEMP
    @GET("/AllStreamsServletAPI")
    ConnexusStream.List nearbyStreams(double latitude, double longitude);

    @GET("/SingleStreamServletAPI")
    ConnexusImage.List streamImages(@Query("streamId") long streamId);

    @POST("/UploadServletAPI")
    void uploadImage();
}
