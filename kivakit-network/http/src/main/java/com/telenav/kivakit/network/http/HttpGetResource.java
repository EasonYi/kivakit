////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.network.http;

import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.http.lexakai.DiagramHttp;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * A resource accessed by HTTP GET at the given network location using the given access constraints.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHttp.class)
@LexakaiJavadoc(complete = true)
public class HttpGetResource extends BaseHttpResource
{
    public HttpGetResource(NetworkLocation location, NetworkAccessConstraints constraints)
    {
        super(location, constraints);
        ensure(location.port().isHttp());
    }

    @Override
    protected HttpUriRequest newRequest()
    {
        var uri = asUri();
        var request = new HttpGet(uri);
        onInitialize(request);
        return request;
    }

    /**
     * Method to allow super classes to add parameters / headers.
     *
     * @param get The get to be sent.
     */
    protected void onInitialize(HttpGet get)
    {
    }
}
