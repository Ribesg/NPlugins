/***************************************************************************
 * Project file:    NPlugins - NCore - BasicHttpClient.java                *
 * Full Class name: fr.ribesg.com.mojang.api.http.BasicHttpClient          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.com.mojang.api.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

public class BasicHttpClient implements HttpClient {

    private static BasicHttpClient instance;

    private BasicHttpClient() {
    }

    public static BasicHttpClient getInstance() {
        if (instance == null) {
            instance = new BasicHttpClient();
        }
        return instance;
    }

    @Override
    public String post(final URL url, final HttpBody body, final List<HttpHeader> headers) throws IOException {
        return this.post(url, null, body, headers);
    }

    @Override
    public String post(final URL url, Proxy proxy, final HttpBody body, final List<HttpHeader> headers) throws IOException {
        if (proxy == null) {
            proxy = Proxy.NO_PROXY;
        }
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
        connection.setRequestMethod("POST");

        for (final HttpHeader header : headers) {
            connection.setRequestProperty(header.getName(), header.getValue());
        }

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        final DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.write(body.getBytes());
        writer.flush();
        writer.close();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        final StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }

        reader.close();
        return response.toString();
    }
}
