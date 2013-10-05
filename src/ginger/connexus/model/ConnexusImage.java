package ginger.connexus.model;

import java.util.ArrayList;

public class ConnexusImage {

    String id;
    public String link;

    public String getUrl() {
        return link;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<ConnexusImage> {
    }

}
