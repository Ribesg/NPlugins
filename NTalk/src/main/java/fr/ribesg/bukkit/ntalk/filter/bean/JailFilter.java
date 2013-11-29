package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class JailFilter extends TimedFilter {

	private final String jailName;

	public JailFilter(final String filteredString, final boolean regex, final long duration, final String jailName) {
		super(filteredString, regex, ChatFilterResult.TEMPORARY_JAIL, duration);
		this.jailName = jailName;
	}

	public String getJailName() {
		return jailName;
	}
}
