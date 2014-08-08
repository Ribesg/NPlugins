/***************************************************************************
 * Project file:    NPlugins - NTalk - JailFilter.java                     *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.JailFilter          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;

import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

import java.util.Map;

/**
 * @author Ribesg
 */
public class JailFilter extends TimedFilter {

    private final String jailName;

    public JailFilter(final String outputString, final String filteredString, final boolean regex, final long duration, final String jailName) {
        super(outputString, filteredString, regex, ChatFilterResult.TEMPORARY_JAIL, duration);
        this.jailName = jailName;
    }

    public String getJailName() {
        return this.jailName;
    }

    // ############ //
    // ## Saving ## //
    // ############ //

    @Override
    public Map<String, Object> getConfigMap() {
        final Map<String, Object> map = super.getConfigMap();
        map.put("jailName", this.jailName);
        return map;
    }

    // ############# //
    // ## Loading ## //
    // ############# //

    public static JailFilter loadFromConfig(final String key, final Map<String, Object> values) {
        try {
            final String filteredString = (String)values.get("filteredString");
            final boolean regex = (boolean)values.get("isRegex");
            final long duration = (int)values.get("duration");
            final String jailName = (String)values.get("jailName");
            return new JailFilter(key, filteredString, regex, duration, jailName);
        } catch (final NullPointerException | ClassCastException e) {
            throw new IllegalArgumentException("Missing value", e);
        }
    }
}
