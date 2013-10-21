/*
 * Copyright (C) 2013 Zach Whaley, Trevor Latson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
