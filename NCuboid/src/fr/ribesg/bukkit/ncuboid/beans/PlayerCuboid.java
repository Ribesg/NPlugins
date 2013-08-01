package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.ncore.utils.NLocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public abstract class PlayerCuboid extends GeneralCuboid {

    public static enum CuboidState {
        NORMAL,
        // Normal Cuboid
        TMPSTATE1,
        // First point selected
        TMPSTATE2 // All points selected, waiting for "/cuboid create" command
    }

    // Identification / informations related
    private String          cuboidName;
    private String          ownerName;
    private CuboidState     state;
    private long            totalSize;
    private String          welcomeMessage;
    private String          farewellMessage;
    private Set<ChunkCoord> chunks;

    // Create a new Cuboid, when user select points etc
    public PlayerCuboid(final String cuboidName, final String ownerName, final String worldName, final CuboidType type) {
        super(worldName, type);
        setCuboidName(cuboidName);
        setOwnerName(ownerName);
        setState(CuboidState.TMPSTATE1);
        setWelcomeMessage(null);
        setFarewellMessage(null);
    }

    // Create a Cuboid from a save
    public PlayerCuboid(final String cuboidName,
                        final String ownerName,
                        final String worldName,
                        final CuboidState state,
                        final long totalSize,
                        final String welcomeMessage,
                        final String farewellMessage,
                        final Set<ChunkCoord> chunks,
                        final CuboidType type,
                        final Rights rights,
                        final int priority,
                        final Flags flags,
                        final FlagAttributes flagAtts) {

        super(worldName, type, rights, priority, flags, flagAtts);
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
    public boolean contains(final NLocation loc) {
        return loc.getWorldName().equals(getWorldName()) && contains(loc.getX(), loc.getY(), loc.getZ());
    }

    public abstract boolean contains(final double x, final double y, final double z);

    // Info
    public String getInfoLine() {
        return "- " + getCuboidName() + " (" + getOwnerName() + ") " + getSizeString();
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

    public String getCuboidName() {
        return cuboidName;
    }

    public void setCuboidName(String cuboidName) {
        this.cuboidName = cuboidName;
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

    public CuboidState getState() {
        return state;
    }

    public void setState(CuboidState state) {
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
