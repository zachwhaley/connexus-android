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

package ginger.connexus.network;

import ginger.connexus.model.ConnexusStream;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class RequestAllStreams extends RetrofitSpiceRequest<ConnexusStream.List, ConnexusApi> {

    public RequestAllStreams() {
        super(ConnexusStream.List.class, ConnexusApi.class);
    }

    @Override
    public ConnexusStream.List loadDataFromNetwork() throws Exception {
        return getService().allStreams();
    }

}
