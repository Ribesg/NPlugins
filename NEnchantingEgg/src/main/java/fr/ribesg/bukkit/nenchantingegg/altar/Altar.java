/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Altar.java                 *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.Altar            *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.MinecraftTime;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.nenchantingegg.NEnchantingEgg;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import fr.ribesg.bukkit.nenchantingegg.item.ItemBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Altar {

    private static final int MAX_RADIUS = 5;

    private final NEnchantingEgg plugin;

    private final NLocation       centerLocation;
    private final Set<ChunkCoord> chunks;

    private AltarState previousState; // Valid only if state == AltarState.IN_TRANSITION
    private AltarState state;

    private String      playerName;
    private ItemBuilder builder;

    public Altar(final NEnchantingEgg plugin, final NLocation loc) {
        this.plugin = plugin;

        this.centerLocation = loc.getBlockLocation();

        this.playerName = null;
        this.builder = null;

        this.chunks = new HashSet<>();

        final int bX = loc.getBlockX();
        final int bZ = loc.getBlockZ();

        for (int x = (int)Math.floor((bX - MAX_RADIUS) / 16.0); x <= Math.floor((bX + MAX_RADIUS) / 16.0); x++) {
            for (int z = (int)Math.floor((bZ - MAX_RADIUS) / 16.0); z <= Math.floor((bZ + MAX_RADIUS) / 16.0); z++) {
                this.chunks.add(new ChunkCoord(x, z, loc.getWorldName()));
            }
        }

        this.setState(AltarState.INVALID);
    }

    public void setState(final AltarState newState) {
        if (newState == AltarState.LOCKED && MinecraftTime.isDayTime(this.centerLocation.getWorld().getTime())) {
            this.state = AltarState.INACTIVE;
        } else {
            if (newState == AltarState.EGG_PROVIDED) {
                this.builder = new ItemBuilder(this);
            }
            this.state = newState;
        }
    }

    public void destroy() {
        this.setState(AltarState.INVALID);
        this.plugin.getAltars().remove(this);
    }

    /**
     * This is called on server stops, prevents Altars from being save with an invalid state
     */
    public void hardResetToInactive(final boolean hurt) {
        this.state = AltarState.INACTIVE;
        final Location loc = this.centerLocation.toBukkitLocation();
        for (final RelativeBlock r : AltarState.getInactiveStateBlocks()) {
            final Block b = loc.clone().add(r.getRelativeLocation()).getBlock();
            b.setType(r.getBlockMaterial());
            b.setData(r.getBlockData());
            if (r.needAdditionalData()) {
                r.setAdditionalData(b);
            }
        }
        loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), hurt ? 5f : 0f, false, false);
        if (this.builder != null) {
            this.builder.popItems();
        }
    }

    public void buildItem(final ItemStack is, final List<ItemStack> items) {
        this.plugin.entering(this.getClass(), "buildItem");

        if (this.state != AltarState.ITEM_PROVIDED) {
            throw new IllegalStateException();
        } else {
            final Location itemDropLocation = this.centerLocation.toBukkitLocation().clone().add(0.5, 3, 0.5);
            if (is != null) {
                final Item i = itemDropLocation.getWorld().dropItem(itemDropLocation, is);
                if (i != null) {
                    i.setPickupDelay(80);
                    i.setVelocity(new Vector(0, -0.25, 0));
                    this.plugin.getItemListener().getItemMap().put(i, this.playerName);
                    this.plugin.debug("Item spawned!");
                } else {
                    this.plugin.error("Unable to spawn the Item!");
                }
            }
            this.builder = null;

            for (final ItemStack item : items) {
                final Item it = itemDropLocation.getWorld().dropItem(itemDropLocation, item);
                if (it != null) {
                    it.setVelocity(new Vector(0, 0.5, 0));
                }
            }
        }

        this.plugin.exiting(this.getClass(), "buildItem");
    }

    /**
     * This method checks that:
     * - All awaited blocks are correctly placed
     * - There is nothing over the altar, i.e. every top block of every x/z coordinates where there is an altar block is an altar block.
     *
     * @return If the altar that may have been constructed at this location is valid
     */
    public boolean isInactiveAltarValid() {
        this.plugin.entering(this.getClass(), "isInactiveAltarValid");

        boolean result = true;

        // First check: if all blocks are here
        for (final RelativeBlock rb : AltarState.getInactiveStateBlocks()) {
            final Location rbLoc = rb.getLocation(this.centerLocation.toBukkitLocation());
            final boolean sameMaterial = rbLoc.getBlock().getType() == rb.getBlockMaterial();
            final boolean sameData = rbLoc.getBlock().getData() == rb.getBlockData();
            final boolean isSkull = rb.getBlockMaterial() == Material.SKULL;
            final boolean isAir = rb.getBlockMaterial() == Material.AIR;
            if ((!sameMaterial || !sameData && !isAir) && !isSkull) {
                if (this.plugin.isDebugEnabled()) {
                    this.plugin.debug("Invalid Altar: should be " + rb.getBlockMaterial() + ':' + rb.getBlockData() + " at location " +
                                      NLocation.toString(rbLoc) + ", is " + rbLoc.getBlock().getType() + ':' + rbLoc.getBlock().getData());
                }
                result = false;
                break;
            }
        }

        if (result) {
            // Second check: if all top blocks are altar blocks
            // TODO: Optimize this?
            final World world = this.centerLocation.getWorld();
            final int cX = this.centerLocation.getBlockX();
            final int cY = this.centerLocation.getBlockY() + 1;
            final int cZ = this.centerLocation.getBlockZ();
            loop:
            for (int x = -MAX_RADIUS; x <= MAX_RADIUS; x++) {
                for (int z = -MAX_RADIUS; z <= MAX_RADIUS; z++) {
                    final int maxY = world.getHighestBlockYAt(cX + x, cZ + z);
                    if (this.isAltarXZ(x, z) && maxY != this.getHighestAltarBlock(x, z, false) + cY) {
                        /** Debug
                         System.out.println("Found:   " + centerLocation.getWorld().getHighestBlockYAt(cX + x, cZ + z));
                         System.out.println("Awaited: " + (getHighestAltarBlock(x, z, false) + cY));
                         System.out.println("At: " + x + ";" + z + " (" + (cX + x) + ";" + (cZ + z) + ")");
                         */
                        this.plugin.debug("Invalid altar: obstructing block located at " + new NLocation(world.getName(), x, maxY, z));
                        result = false;
                        break loop;
                    }
                }
            }
        }

        this.plugin.exiting(this.getClass(), "isInactiveAltarValid");
        return result;
    }

    public Location getEggLocation() {
        return this.centerLocation.toBukkitLocation().add(0, 1, 0);
    }

    /**
     * @param loc A Location
     *
     * @return True if the given loc is the location of where players have to put the DragonEgg
     */
    public boolean isEggPosition(final Location loc) {
        final int x = loc.getBlockX() - this.centerLocation.getBlockX();
        final int y = loc.getBlockY() - this.centerLocation.getBlockY();
        final int z = loc.getBlockZ() - this.centerLocation.getBlockZ();
        return x == 0 && y == 1 && z == 0;
    }

    /**
     * @param loc A Location
     *
     * @return True if the given loc is the location of where players have to put the Skull
     */
    public boolean isSkullPosition(final Location loc) {
        final int x = loc.getBlockX() - this.centerLocation.getBlockX();
        final int y = loc.getBlockY() - this.centerLocation.getBlockY();
        final int z = loc.getBlockZ() - this.centerLocation.getBlockZ();
        return x == -2 && y == 2 && z == 0;
    }

    /**
     * @param loc the Location of the block to place
     *
     * @return True if this altar prevents the placement of the block
     */
    public boolean preventsBlockPlacement(final Location loc) {
        final int x = loc.getBlockX() - this.centerLocation.getBlockX();
        final int y = loc.getBlockY() - this.centerLocation.getBlockY();
        final int z = loc.getBlockZ() - this.centerLocation.getBlockZ();
        return this.isAltarXZ(x, z) && this.getHighestAltarBlock(x, z, true) <= y;
    }

    /**
     * @param event A BlockBreakEvent
     *
     * @return True if this altar prevents the destruction of the block
     */
    public boolean preventsBlockDestruction(final BlockBreakEvent event) {
        final Location loc = event.getBlock().getLocation();
        final int x = loc.getBlockX() - this.centerLocation.getBlockX();
        final int y = loc.getBlockY() - this.centerLocation.getBlockY();
        final int z = loc.getBlockZ() - this.centerLocation.getBlockZ();
        return this.isAltarXYZ(x, y, z);
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
        } else if (this.state == AltarState.EGG_PROVIDED || this.state == AltarState.ITEM_PROVIDED ||
                   this.state == AltarState.IN_TRANSITION && (this.previousState == AltarState.EGG_PROVIDED || this.previousState == AltarState.ITEM_PROVIDED)) {
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
        return this.builder;
    }

    public NLocation getCenterLocation() {
        return this.centerLocation;
    }

    public Set<ChunkCoord> getChunks() {
        return this.chunks;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(final String playerName) {
        this.playerName = playerName;
    }

    public AltarState getPreviousState() {
        return this.previousState;
    }

    public AltarState getState() {
        return this.state;
    }

    public void setPreviousState(final AltarState previousState) {
        this.previousState = previousState;
    }

    public NEnchantingEgg getPlugin() {
        return this.plugin;
    }
}
