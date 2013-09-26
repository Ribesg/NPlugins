package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public abstract class PlayerRegion extends GeneralRegion {

	public static enum RegionState {
		/** Normal Region */
		NORMAL,

		/** First point selected */
		TMPSTATE1,

		/** All points selected, waiting for "/cuboid create" command */
		TMPSTATE2;
	}

	private String          regionName;
	private String          ownerName;
	private RegionState     state;
	private long            totalSize;
	private String          welcomeMessage;
	private String          farewellMessage;
	private Set<ChunkCoord> chunks;

	/** Create a new Region, when user select points etc */
	public PlayerRegion(final String regionName, final String ownerName, final String worldName, final RegionType type) {
		super(worldName, type);
		setRegionName(regionName);
		setOwnerName(ownerName);
		setState(RegionState.TMPSTATE1);
		setWelcomeMessage(null);
		setFarewellMessage(null);
	}

	/** Create a Region from a save */
	public PlayerRegion(final String regionName,
	                    final String ownerName,
	                    final String worldName,
	                    final RegionState state,
	                    final long totalSize,
	                    final String welcomeMessage,
	                    final String farewellMessage,
	                    final RegionType type,
	                    final Rights rights,
	                    final int priority,
	                    final Flags flags,
	                    final FlagAttributes flagAtts) {

		super(worldName, type, rights, priority, flags, flagAtts);
		setRegionName(regionName);
		setOwnerName(ownerName);
		setState(state);
		setTotalSize(totalSize);
		setWelcomeMessage(welcomeMessage);
		setFarewellMessage(farewellMessage);
	}

	/**
	 * Called on a Selection region to transform it into an actual Region
	 *
	 * @param regionName the name of the new Region
	 */
	public void create(final String regionName) {
		setRegionName(regionName);
		setState(RegionState.NORMAL);
	}

	// Location check
	@Override
	public boolean contains(final NLocation loc) {
		return loc.getWorldName().equals(getWorldName()) && contains(loc.getX(), loc.getY(), loc.getZ());
	}

	public abstract boolean contains(final double x, final double y, final double z);

	// Info
	public String getInfoLine() {
		return "- " + getRegionName() + " (" + getOwnerName() + ") " + getSizeString();
	}

	public abstract String getSizeString();

	public boolean isOwner(CommandSender sender) {
		return sender instanceof Player && isOwner(sender.getName());
	}

	public boolean isOwner(String playerName) {
		return this.ownerName.equals(playerName);
	}

	public Set<ChunkCoord> getChunks() {
		return chunks;
	}

	public void setChunks(Set<ChunkCoord> chunks) {
		this.chunks = chunks;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getFarewellMessage() {
		return farewellMessage;
	}

	public void setFarewellMessage(String farewellMessage) {
		this.farewellMessage = farewellMessage;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public RegionState getState() {
		return state;
	}

	public void setState(RegionState state) {
		this.state = state;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}
}
