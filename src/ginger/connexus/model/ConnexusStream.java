package ginger.connexus.model;

import java.util.ArrayList;

public class ConnexusStream {

    long id;
    String name;
    String tags;
    String cover_url;

    public ConnexusStream() {
    }

    public String getCoverUrl() {
        return cover_url;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isMatch(String query) {
        boolean match = false;
        match |= name.toLowerCase().contains(query.toLowerCase());
        match |= tags.toLowerCase().contains(query.toLowerCase());
        return match;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<ConnexusStream> {
    }

}
