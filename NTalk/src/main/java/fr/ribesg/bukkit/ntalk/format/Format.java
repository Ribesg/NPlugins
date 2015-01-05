/***************************************************************************
 * Project file:    NPlugins - NTalk - Format.java                         *
 * Full Class name: fr.ribesg.bukkit.ntalk.format.Format                   *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.format;

public class Format {

    public enum FormatType {
        GROUP,
        PLAYER,
    }

    private final FormatType type;
    private final String     name;
    private final String     prefix;
    private final String     suffix;

    public Format(final FormatType type, final String name, final String prefix, final String suffix) {
        this.type = type;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public FormatType getType() {
        return this.type;
    }
}
