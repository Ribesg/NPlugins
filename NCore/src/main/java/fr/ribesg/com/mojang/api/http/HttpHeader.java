/***************************************************************************
 * Project file:    NPlugins - NCore - HttpHeader.java                     *
 * Full Class name: fr.ribesg.com.mojang.api.http.HttpHeader               *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.com.mojang.api.http;

public class HttpHeader {

    private String name;
    private String value;

    public HttpHeader(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
