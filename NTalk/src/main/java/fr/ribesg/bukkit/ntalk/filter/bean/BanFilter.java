package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class BanFilter extends TimedFilter {

	public BanFilter(final String filteredString, final boolean regex, final long duration) {
		super(filteredString, regex, ChatFilterResult.TEMPORARY_BAN, duration);
	}
}
