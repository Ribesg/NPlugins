package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class DenyFilter extends Filter {

	public DenyFilter(final String filteredString, final boolean regex) {
		super(filteredString, regex, ChatFilterResult.DENY);
	}
}
