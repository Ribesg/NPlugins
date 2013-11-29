package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public abstract class TimedFilter extends Filter {

	private final long duration;

	protected TimedFilter(final String filteredString, final boolean regex, final ChatFilterResult responseType, final long duration) {
		super(filteredString, regex, responseType);
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}
}
