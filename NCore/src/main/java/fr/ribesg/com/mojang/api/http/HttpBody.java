/***************************************************************************
 * Project file:    NPlugins - NCore - HttpBody.java                       *
 * Full Class name: fr.ribesg.com.mojang.api.http.HttpBody                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.com.mojang.api.http;

public class HttpBody {

    public static final byte[] EMPTY = new byte[0];
    private final String bodyString;

    public HttpBody(final String bodyString) {
        this.bodyString = bodyString;
    }

    public byte[] getBytes() {
        return this.bodyString != null ? this.bodyString.getBytes() : EMPTY;
    }
}
