package fr.ribesg.bukkit.nworld.world;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nworld.NWorld;

/** @author Ribesg */
public class AdditionalWorld extends GeneralWorld {

	private final long               seed;
	private       boolean            hasNether;
	private       AdditionalSubWorld netherWorld;
	private       boolean            hasEnd;
	private       AdditionalSubWorld endWorld;

	public AdditionalWorld(NWorld instance,
	                       String worldName,
	                       long seed,
	                       NLocation spawnLocation,
	                       String requiredPermission,
	                       boolean enabled,
	                       boolean hidden,
	                       boolean hasNether,
	                       boolean hasEnd) {
		super(instance, worldName, spawnLocation, requiredPermission, enabled, hidden);
		this.seed = seed;
		this.hasNether = hasNether;
		netherWorld = null;
		this.hasEnd = hasEnd;
		endWorld = null;
		setType(WorldType.ADDITIONAL);
	}

	public long getSeed() {
		return seed;
	}

	public boolean hasNether() {
		return hasNether;
	}

	public void setNether(boolean hasNether) {
		this.hasNether = hasNether;
	}

	public boolean hasEnd() {
		return hasEnd;
	}

	public void setEnd(boolean hasEnd) {
		this.hasEnd = hasEnd;
	}

	public AdditionalSubWorld getEndWorld() {
		return endWorld;
	}

	public void setEndWorld(AdditionalSubWorld endWorld) {
		this.endWorld = endWorld;
	}

	public AdditionalSubWorld getNetherWorld() {
		return netherWorld;
	}

	public void setNetherWorld(AdditionalSubWorld netherWorld) {
		this.netherWorld = netherWorld;
	}

	public boolean isMalformed() {
		return false;
	}
}
