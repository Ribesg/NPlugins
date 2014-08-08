/***************************************************************************
 * Project file:    NPlugins - NTalk - ReplaceFilter.java                  *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.ReplaceFilter       *
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
public class ReplaceFilter extends Filter {

    private final String replacement;

    public ReplaceFilter(final String outputString, final String filteredString, final boolean regex, final String replacement) {
        super(outputString, filteredString, regex, ChatFilterResult.REPLACE);
        this.replacement = replacement;
    }

    public String getReplacement() {
        return this.replacement;
    }

    // ############ //
    // ## Saving ## //
    // ############ //

    @Override
    public Map<String, Object> getConfigMap() {
        final Map<String, Object> map = super.getConfigMap();
        map.put("replacement", this.replacement);
        return map;
    }

    // ############# //
    // ## Loading ## //
    // ############# //

    public static ReplaceFilter loadFromConfig(final String key, final Map<String, Object> values) {
        try {
            final String filteredString = (String)values.get("filteredString");
            final boolean regex = (boolean)values.get("isRegex");
            final String replacement = (String)values.get("replacement");
            return new ReplaceFilter(key, filteredString, regex, replacement);
        } catch (final NullPointerException | ClassCastException e) {
            throw new IllegalArgumentException("Missing value", e);
        }
    }
}
