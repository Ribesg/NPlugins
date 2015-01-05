/***************************************************************************
 * Project file:    NPlugins - NCore - HttpClient.java                     *
 * Full Class name: fr.ribesg.com.mojang.api.http.HttpClient               *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.com.mojang.api.http;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

public interface HttpClient {

    public String post(final URL url, final HttpBody body, final List<HttpHeader> headers) throws IOException;

    public String post(final URL url, final Proxy proxy, final HttpBody body, final List<HttpHeader> headers) throws IOException;
}
