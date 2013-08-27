package fr.ribesg.bukkit.nenchantingegg.altar;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.common.Time;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import fr.ribesg.bukkit.nenchantingegg.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class Altar {

	private final static int MAX_RADIUS = 5;

	private final NEnchantingEgg plugin;

	private final NLocation       centerLocation;
	private final Set<ChunkCoord> chunks;

	private AltarState previousState; // Valid only if state == AltarState.IN_TRANSITION
	private AltarState state;

	private String      playerName;
	private ItemBuilder builder;

	public Altar(NEnchantingEgg plugin, final NLocation loc) {
		this.plugin = plugin;

		centerLocation = loc.getBlockLocation();

		playerName = null;
		builder = null;

		chunks = new HashSet<>();

		final int bX = loc.getBlockX();
		final int bZ = loc.getBlockZ();

		for (int x = (int) Math.floor((bX - MAX_RADIUS) / 16.0); x <= Math.floor((bX + MAX_RADIUS) / 16.0); x++) {
			for (int z = (int) Math.floor((bZ - MAX_RADIUS) / 16.0); z <= Math.floor((bZ + MAX_RADIUS) / 16.0); z++) {
				chunks.add(new ChunkCoord(x, z, loc.getWorldName()));
			}
		}

		setState(AltarState.INVALID);
	}

	public void setState(final AltarState newState) {
		if (newState == AltarState.LOCKED && Time.isDayTime(centerLocation.getWorld().getTime())) {
			state = AltarState.INACTIVE;
		} else {
			if (newState == AltarState.EGG_PROVIDED) {
				builder = new ItemBuilder(this);
			}
			state = newState;
		}
	}

	public void destroy() {
		setState(AltarState.INVALID);
		plugin.getAltars().remove(this);
	}

	/** This is called on server stops, prevents Altars from being save with an invalid state */
	public void hardResetToInactive() {
		this.state = AltarState.INACTIVE;
		Location loc = getCenterLocation().toBukkitLocation();
		for (final RelativeBlock r : AltarState.getInactiveStateBlocks()) {
			final Block b = loc.clone().add(r.getRelativeLocation()).getBlock();
			b.setType(r.getBlockMaterial());
			b.setData(r.getBlockData());
			if (r.needAdditionalData()) {
				r.setAdditionalData(b);
			}
		}
	}

	public void buildItem(final ItemStack is) {
		if (state != AltarState.ITEM_PROVIDED) {
			throw new IllegalStateException();
		} else {
			final Location itemDropLocation = centerLocation.toBukkitLocation().clone().add(0.5, 3, 0.5);
			final Item i = itemDropLocation.getWorld().dropItem(itemDropLocation, is);
			if (i != null) {
				i.setPickupDelay(80);
				i.setVelocity(new Vector(0, -0.25, 0));
				plugin.getItemListener().getItemMap().put(i, playerName);
			} else {
				plugin.getLogger().severe("Unable to spawn the Item!");
			}
			builder = null;
		}
	}

	/**
	 * This method checks that:
	 * - All awaited blocks are correctly placed
	 * - There is nothing over the altar, i.e. every top block of every x/z coordinates where there is an altar block is an altar block.
	 *
	 * @return If the altar that may have been constructed at this location is valid
	 */
	public boolean isInactiveAltarValid() {
		// First check: if all blocks are here
		for (final RelativeBlock rb : AltarState.getInactiveStateBlocks()) {
			final Location rbLoc = rb.getLocation(centerLocation.toBukkitLocation());
			if ((rbLoc.getBlock().getType() != rb.getBlockMaterial() || rbLoc.getBlock().getData() != rb.getBlockData()) &&
			    rb.getBlockMaterial() != Material.SKULL) {
				return false;
			}
		}

		// Second check: if all top blocks are altar blocks
		// TODO: Optimize this?
		final int cX = centerLocation.getBlockX();
		final int cY = centerLocation.getBlockY() + 1;
		final int cZ = centerLocation.getBlockZ();
		for (int x = -MAX_RADIUS; x <= MAX_RADIUS; x++) {
			for (int z = -MAX_RADIUS; z <= MAX_RADIUS; z++) {
				if (isAltarXZ(x, z) &&
				    centerLocation.getWorld().getHighestBlockYAt(cX + x, cZ + z) != getHighestAltarBlock(x, z, false) + cY) {
					/** Debug
					 System.out.println("Found:   " + centerLocation.getWorld().getHighestBlockYAt(cX + x, cZ + z));
					 System.out.println("Awaited: " + (getHighestAltarBlock(x, z, false) + cY));
					 System.out.println("At: " + x + ";" + z + " (" + (cX + x) + ";" + (cZ + z) + ")");
					 */
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param loc A Location
	 *
	 * @return True if the given loc is the location of where players have to put the DragonEgg
	 */
	public boolean isEggPosition(final Location loc) {
		final int x = loc.getBlockX() - centerLocation.getBlockX();
		final int y = loc.getBlockY() - centerLocation.getBlockY();
		final int z = loc.getBlockZ() - centerLocation.getBlockZ();
		return x == 0 && y == 1 && z == 0;
	}

	/**
	 * @param loc A Location
	 *
	 * @return True if the given loc is the location of where players have to put the Skull
	 */
	public boolean isSkullPosition(final Location loc) {
		final int x = loc.getBlockX() - centerLocation.getBlockX();
		final int y = loc.getBlockY() - centerLocation.getBlockY();
		final int z = loc.getBlockZ() - centerLocation.getBlockZ();
		return x == -2 && y == 2 && z == 0;
	}

	/**
	 * @param event A BlockPlaceEvent
	 *
	 * @return True if this altar prevents the placement of the block
	 */
	public boolean preventsBlockPlacement(final BlockPlaceEvent event) {
		final Location loc = event.getBlockPlaced().getLocation();
		final int x = loc.getBlockX() - centerLocation.getBlockX();
		final int y = loc.getBlockY() - centerLocation.getBlockY();
		final int z = loc.getBlockZ() - centerLocation.getBlockZ();
		return isAltarXZ(x, z) && getHighestAltarBlock(x, z, true) <= y;
	}

	/**
	 * @param event A BlockBreakEvent
	 *
	 * @return True if this altar prevents the destruction of the block
	 */
	public boolean preventsBlockDestruction(final BlockBreakEvent event) {
		final Location loc = event.getBlock().getLocation();
		final int x = loc.getBlockX() - centerLocation.getBlockX();
		final int y = loc.getBlockY() - centerLocation.getBlockY();
		final int z = loc.getBlockZ() - centerLocation.getBlockZ();
		return isAltarXYZ(x, y, z);
	}

	/**
	 * @param x RelativeLocation x to center
	 * @param z RelativeLocation z to center
	 *
	 * @return True if the coords correspond to a 2D position of a Altar block
	 */
	private boolean isAltarXZ(final int x, final int z) {
		return x >= -3 && x <= 3 && z >= -3 && z <= 3 ||
		       Math.abs(x) == 4 && z >= -2 && z <= 2 ||
		       Math.abs(z) == 4 && x >= -2 && x <= 2;
	}

	/**
	 * @param x RelativeLocation x to center
	 * @param y RelativeLocation y to center
	 * @param z RelativeLocation z to center
	 *
	 * @return True if the coords correspond to a 3D position of a Altar block
	 */
	public boolean isAltarXYZ(final int x, final int y, final int z) {
		for (final RelativeBlock rb : AltarState.getInactiveStateBlocks()) {
			if (rb.getRelativeLocation().getBlockX() == x &&
			    rb.getRelativeLocation().getBlockY() == y &&
			    rb.getRelativeLocation().getBlockZ() == z) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param x RelativeLocation x to center
	 * @param z RelativeLocation z to center
	 *
	 * @return The highest block Y coordinate
	 */
	private int getHighestAltarBlock(final int x, final int z, final boolean skullPlaced) {
		if (x == -3 && z == 0) {
			return 4;
		} else if (x == -2 && Math.abs(z) == 2) {
			return 3;
		} else if (x == 0 && Math.abs(z) == 3) {
			return 2;
		} else if (x == 2 && Math.abs(z) == 2) {
			return 1;
		} else if (x == -2 && z == 0) {
			return skullPlaced ? 2 : 1;
		} else if (state == AltarState.EGG_PROVIDED || state == AltarState.ITEM_PROVIDED ||
		           state == AltarState.IN_TRANSITION &&
		           (previousState == AltarState.EGG_PROVIDED || previousState == AltarState.ITEM_PROVIDED)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param skullLocation The location of the placed skull
	 *
	 * @return The location of the center of an altar having this skull as a valid block
	 */
	public static NLocation getCenterFromSkullLocation(final Location skullLocation) {
		return new NLocation(skullLocation.clone().add(2, -2, 0));
	}

	public ItemBuilder getBuilder() {
		return builder;
	}

	public NLocation getCenterLocation() {
		return centerLocation;
	}

	public Set<ChunkCoord> getChunks() {
		return chunks;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public AltarState getPreviousState() {
		return previousState;
	}

	public AltarState getState() {
		return state;
	}

	public void setPreviousState(AltarState previousState) {
		this.previousState = previousState;
	}

	public NEnchantingEgg getPlugin() {
		return plugin;
	}
}
