package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class DivineFilter extends TimedFilter {

	private final int minHealth;
	private final int entityAmount;

	public DivineFilter(final String filteredString,
	                    final boolean regex,
	                    final long duration,
	                    final int minHealth,
	                    final int entityAmount) {
		super(filteredString, regex, ChatFilterResult.DIVINE_PUNISHMENT, duration);
		this.minHealth = minHealth;
		this.entityAmount = entityAmount;
	}

	public int getMinHealth() {
		return minHealth;
	}

	public int getEntityAmount() {
		return entityAmount;
	}
}
