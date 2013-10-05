package ginger.connexus.network;

import ginger.connexus.model.ConnexusImage;
import ginger.connexus.model.ConnexusStream;
import retrofit.http.GET;
import retrofit.http.Path;

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

    @GET("/SingleStreamServletAPI?streamId={streamid}")
    ConnexusImage.List streamImages(@Path("streamid") long streamid);
}
