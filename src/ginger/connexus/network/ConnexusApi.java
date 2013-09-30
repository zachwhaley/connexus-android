package ginger.connexus.network;

import ginger.connexus.model.ImageWrapper;
import retrofit.http.GET;

/**
 * Connexus REST API as Java Interface
 * @author zwhaley
 */
public interface ConnexusApi {

    @GET("") // TODO API call
    ImageWrapper.List allStreams();

    @GET("") // TODO API call
    ImageWrapper.List userStreams();

    @GET("") // TODO API call
    ImageWrapper.List subscriptionStreams();

    @GET("") // TODO API call
    ImageWrapper.List nearbyStreams();
}
