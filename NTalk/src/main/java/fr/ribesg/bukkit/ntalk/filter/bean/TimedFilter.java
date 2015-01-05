/***************************************************************************
 * Project file:    NPlugins - NTalk - TimedFilter.java                    *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.TimedFilter         *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;

import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

import java.util.Map;

/**
 * @author Ribesg
 */
public abstract class TimedFilter extends Filter {

    private final long duration;

    protected TimedFilter(final String outputString, final String filteredString, final boolean regex, final ChatFilterResult responseType, final long duration) {
        super(outputString, filteredString, regex, responseType);
        this.duration = duration;
    }

    public long getDuration() {
        return this.duration;
    }

    // ############ //
    // ## Saving ## //
    // ############ //

    @Override
    public Map<String, Object> getConfigMap() {
        final Map<String, Object> map = super.getConfigMap();
        map.put("duration", this.duration);
        return map;
    }
}
