package ginger.connexus.network;

import ginger.connexus.model.ConnexusImage;
import ginger.connexus.model.ConnexusStream;
import retrofit.http.GET;

/**
 * Connexus REST API as Java Interface
 * 
 * @author zwhaley
 */
public interface ConnexusApi {

	@GET("")
	// TODO API call
	ConnexusStream.List allStreams();

	@GET("")
	// TODO API call
	ConnexusStream.List userStreams();

	@GET("")
	// TODO API call
	ConnexusStream.List subscribedStreams();

	@GET("")
	// TODO API call
	ConnexusStream.List nearbyStreams();

	@GET("")
	// TODO API call
	ConnexusImage.List streamImages();
}
