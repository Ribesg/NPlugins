package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
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
		super(worldName, type, 1);
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

	public boolean isOwner(final CommandSender sender) {
		return sender instanceof Player && isOwner(sender.getName());
	}

	public boolean isOwner(final String playerName) {
		return this.ownerName.equals(playerName);
	}

	public Set<ChunkCoord> getChunks() {
		return chunks;
	}

	public void setChunks(final Set<ChunkCoord> chunks) {
		this.chunks = chunks;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(final String regionName) {
		this.regionName = regionName;
	}

	public String getFarewellMessage() {
		return farewellMessage;
	}

	public void setFarewellMessage(final String farewellMessage) {
		this.farewellMessage = farewellMessage;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(final String ownerName) {
		this.ownerName = ownerName;
	}

	public RegionState getState() {
		return state;
	}

	public void setState(final RegionState state) {
		this.state = state;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(final long totalSize) {
		this.totalSize = totalSize;
	}

	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(final String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@Override
	public boolean isUser(final CommandSender sender) {
		return isOwner(sender.getName()) || super.isUser(sender);
	}

	@Override
	public boolean isUserName(final String name) {
		return isOwner(name) || super.isUserName(name);
	}

	@Override
	public boolean isAdmin(final CommandSender sender) {
		return isOwner(sender.getName()) || super.isAdmin(sender);
	}

	@Override
	public boolean isAdminName(final String name) {
		return isOwner(name) || super.isAdminName(name);
	}

	@Override
	public Set<String> getUsers() {
		final Set<String> users = super.getUsers();
		final Set<String> result = new HashSet<>();
		if (users != null) {
			result.addAll(users);
		}
		result.add(getOwnerName());
		return result;
	}

	@Override
	public Set<String> getAdmins() {
		final Set<String> admins = super.getAdmins();
		final Set<String> result = new HashSet<>();
		if (admins != null) {
			result.addAll(admins);
		}
		result.add(getOwnerName());
		return result;
	}
}
