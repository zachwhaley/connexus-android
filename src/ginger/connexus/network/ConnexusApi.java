package ginger.connexus.network;

import ginger.connexus.model.ConnexusImage;
import ginger.connexus.model.ConnexusStream;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Connexus REST API as Java Interface
 * 
 * @author zwhaley
 */
public interface ConnexusApi {

    @GET("/allstreams")
    ConnexusStream.List allStreams();

    @GET("/mystreams")
    ConnexusStream.List subscribedStreams(@Query("email") String email);

    @GET("/nearbystreams")
    ConnexusStream.List nearbyStreams(@Query("latitude") float latitude, @Query("longitude") float longitude);

    @GET("/images")
    ConnexusImage.List streamImages(@Query("stream") long streamId);
}
