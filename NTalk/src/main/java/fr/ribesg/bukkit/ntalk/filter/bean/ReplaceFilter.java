package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class ReplaceFilter extends Filter {

	private final String replacement;

	public ReplaceFilter(final String filteredString, final boolean regex, final String replacement) {
		super(filteredString, regex, ChatFilterResult.REPLACE);
		this.replacement = replacement;
	}

	public String getReplacement() {
		return replacement;
	}
}
