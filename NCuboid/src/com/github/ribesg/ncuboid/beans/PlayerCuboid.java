package com.github.ribesg.ncuboid.beans;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.World;

import com.github.ribesg.ncore.nodes.cuboid.beans.FlagAttributes;
import com.github.ribesg.ncore.nodes.cuboid.beans.Flags;
import com.github.ribesg.ncore.nodes.cuboid.beans.Rights;

public abstract class PlayerCuboid extends GeneralCuboid {

    public static enum CuboidState {
        NORMAL, // Normal Cuboid
        TMPSTATE1, // First point selected
        TMPSTATE2 // All points selected, waiting for "/cuboid create" command
    }

    // Identification / informations related
    @Getter @Setter private String        cuboidName;
    @Getter @Setter private String        ownerName;
    @Getter @Setter private CuboidState   state;
    @Getter @Setter private long          totalSize;
    @Getter @Setter private String        welcomeMessage;
    @Getter @Setter private String        farewellMessage;
    @Getter @Setter private Set<ChunkKey> chunks;

    // Create a new Cuboid, when user select points etc
    public PlayerCuboid(final String cuboidName, final String ownerName, final World world, final CuboidType type) {
        super(world, type);
        setCuboidName(cuboidName);
        setOwnerName(ownerName);
        setState(CuboidState.TMPSTATE1);
        setWelcomeMessage(null);
        setFarewellMessage(null);
    }

    // Create a Cuboid from a save
    public PlayerCuboid(
            final String cuboidName,
            final String ownerName,
            final World world,
            final CuboidState state,
            final long totalSize,
            final String welcomeMessage,
            final String farewellMessage,
            final Set<ChunkKey> chunks,
            final CuboidType type,
            final Rights rights,
            final int priority,
            final Flags flags,
            final FlagAttributes flagAtts) {

        super(world, type, rights, priority, flags, flagAtts);
        setCuboidName(cuboidName);
        setOwnerName(ownerName);
        setState(state);
        setTotalSize(totalSize);
        setWelcomeMessage(welcomeMessage);
        setFarewellMessage(farewellMessage);
        setChunks(chunks);
    }

    public void create(final String cuboidName) {
        setCuboidName(cuboidName);
        setState(CuboidState.NORMAL);
    }

    // Location check
    @Override
    public boolean contains(final Location loc) {
        return loc.getWorld().getName().equals(getWorld().getName()) && contains(loc.getX(), loc.getY(), loc.getZ());
    }

    public abstract boolean contains(final double x, final double y, final double z);

    // Info
    public String getInfoLine() {
        return "- " + getCuboidName() + " (" + getOwnerName() + ") " + getSizeString();
    }

    public abstract String getSizeString();
}
