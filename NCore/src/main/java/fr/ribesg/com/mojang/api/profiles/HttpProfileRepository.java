/***************************************************************************
 * Project file:    NPlugins - NCore - HttpProfileRepository.java          *
 * Full Class name: fr.ribesg.com.mojang.api.profiles.HttpProfileRepository*
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.com.mojang.api.profiles;

import com.google.gson.Gson;

import fr.ribesg.com.mojang.api.http.BasicHttpClient;
import fr.ribesg.com.mojang.api.http.HttpBody;
import fr.ribesg.com.mojang.api.http.HttpClient;
import fr.ribesg.com.mojang.api.http.HttpHeader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpProfileRepository implements ProfileRepository {

    // You're not allowed to request more than 100 profiles per go.
    private static final int PROFILES_PER_REQUEST = 100;

    private static final Gson GSON = new Gson();
    private final String     agent;
    private final HttpClient client;

    public HttpProfileRepository(final String agent) {
        this(agent, BasicHttpClient.getInstance());
    }

    public HttpProfileRepository(final String agent, final HttpClient client) {
        this.agent = agent;
        this.client = client;
    }

    @Override
    public Profile[] findProfilesByNames(final String... names) {
        final List<Profile> profiles = new ArrayList<>();
        try {

            final List<HttpHeader> headers = new ArrayList<>();
            headers.add(new HttpHeader("Content-Type", "application/json"));

            final int namesCount = names.length;
            int start = 0;
            int i = 0;
            do {
                int end = PROFILES_PER_REQUEST * (i + 1);
                if (end > namesCount) {
                    end = namesCount;
                }
                final String[] namesBatch = Arrays.copyOfRange(names, start, end);
                final HttpBody body = getHttpBody(namesBatch);
                final Profile[] result = this.post(this.getProfilesUrl(), body, headers);
                profiles.addAll(Arrays.asList(result));

                start = end;
                i++;
            } while (start < namesCount);
        } catch (final Exception e) {
            // TODO: logging and allowing consumer to react?
        }

        return profiles.toArray(new Profile[profiles.size()]);
    }

    private URL getProfilesUrl() throws MalformedURLException {
        // To lookup Minecraft profiles, agent should be "minecraft"
        return new URL("https://api.mojang.com/profiles/" + this.agent);
    }

    private Profile[] post(final URL url, final HttpBody body, final List<HttpHeader> headers) throws IOException {
        final String response = this.client.post(url, body, headers);
        return GSON.fromJson(response, Profile[].class);
    }

    private static HttpBody getHttpBody(final String... namesBatch) {
        return new HttpBody(GSON.toJson(namesBatch));
    }
}
