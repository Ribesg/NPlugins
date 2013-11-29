package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class MuteFilter extends TimedFilter {

	public MuteFilter(final String filteredString, final boolean regex, final long duration) {
		super(filteredString, regex, ChatFilterResult.TEMPORARY_MUTE, duration);
	}
}
