package ginger.connexus.network;

import ginger.connexus.model.ConnexusImage;

import java.io.File;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

public class UploadImage {
    private static final String API_URL = "http://connexus-api.appspot.com";

    interface UploadApi {
        @Multipart
        @POST("/_ah/upload/{path1}/{path2}/")
        void uploadImage(@Path("path1") String path1, @Path("path2") String path2,
                @Part("stream") long stream,
                @Part("latitude") float latitude,
                @Part("longitude") float longitude,
                @Part("image") TypedFile image,
                Callback<ConnexusImage> cb);
    }

    private final String uploadurl;
    private final long stream;
    private final float latitude;
    private final float longitude;
    private final TypedFile image;
    private final Callback<ConnexusImage> callback;

    public UploadImage(String uploadUrl, long streamId, float lat, float lng, File file, Callback<ConnexusImage> cb) {
        this.uploadurl = uploadUrl;
        this.stream = streamId;
        this.latitude = lat;
        this.longitude = lng;
        this.image = new TypedFile("image/jpg", file);
        this.callback = cb;
    }

    public void post() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(API_URL)
                .build();

        UploadApi upload = restAdapter.create(UploadApi.class);
        String[] paths = uploadurl.split("/");
        upload.uploadImage(paths[0], paths[1], stream, latitude, longitude, image, callback);
    }

}
