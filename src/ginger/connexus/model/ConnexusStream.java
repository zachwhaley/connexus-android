package ginger.connexus.model;

import java.util.ArrayList;

import android.location.Location;

public class ConnexusStream {

    long id;
    public String cover;
    public ConnexusImage.List images;
    public Location location;

    public ConnexusStream() {
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<ConnexusStream> {
    }

}
