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

package com.telenav.kivakit.network.http.secure;

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Wrapper for a DefaultHttpClient which ignores all certificate errors. This client should be used when connecting to a
 * site which has an invalid certificate.
 * </p>
 *
 * @author ericg
 * @author jonathanl
 */
@SuppressWarnings("deprecation")
@LexakaiJavadoc(complete = true)
class InvalidCertificateTrustingHttpClient extends DefaultHttpClient
{
    public InvalidCertificateTrustingHttpClient()
    {
        try
        {
            var context = SSLContext.getInstance("TLS");
            var trustManager = new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string)
                {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string)
                {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };
            context.init(null, new TrustManager[] { trustManager }, null);

            var socketFactory = new SSLSocketFactory(context, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            var schemeRegistry = getConnectionManager().getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", 443, socketFactory));
        }
        catch (Exception e)
        {
            throw new Problem(e, "Unable to create trusting certificate").asException();
        }
    }
}
