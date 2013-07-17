package fr.ribesg.bukkit.ncuboid.beans;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Set;

public abstract class GeneralCuboid extends Cuboid {

    public static enum CuboidType {
        // RectCuboid
        RECT,

        // WorldCuboid
        WORLD,
    }

    // Identification / informations related
    private World      world;
    private CuboidType type;

    // Protection related
    private final Rights rights;
    private       int    priority;

    // Flags related
    private final Flags          flags;
    private final FlagAttributes flagAtts;

    // Create a new Cuboid, when user select points etc
    public GeneralCuboid(final World world, final CuboidType type) {
        setWorld(world);
        setType(type);
        rights = new Rights();
        setPriority(0);
        flags = new Flags();
        flagAtts = new FlagAttributes();
    }

    public GeneralCuboid(final World world,
                         final CuboidType type,
                         final Rights rights,
                         final int priority,
                         final Flags flags,
                         final FlagAttributes flagAtts) {
        setWorld(world);
        setType(type);
        this.rights = rights;
        setPriority(priority);
        this.flags = flags;
        this.flagAtts = flagAtts;
    }

    // Location check
    public abstract boolean contains(final Location loc);

    public abstract String getCuboidName();

    public abstract long getTotalSize();

    public FlagAttributes getFlagAtts() {
        return flagAtts;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public CuboidType getType() {
        return type;
    }

    public void setType(CuboidType type) {
        this.type = type;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean getFlag(Flag f) {
        return flags.getFlag(f);
    }

    public void setFlag(Flag f, boolean b) {
        flags.setFlag(f, b);
    }

    public void allowCommand(String command) {
        rights.allowCommand(command);
    }

    public void denyGroup(String groupName) {
        rights.denyGroup(groupName);
    }

    public boolean isAllowedPlayer(Player p) {
        return rights.isAllowedPlayer(p);
    }

    public void allowGroup(String groupName) {
        rights.allowGroup(groupName);
    }

    public void denyCommand(String command) {
        rights.denyCommand(command);
    }

    public void allowPlayer(String playerName) {
        rights.allowPlayer(playerName);
    }

    public boolean isAllowedPlayerName(String playerName) {
        return rights.isAllowedPlayerName(playerName);
    }

    public boolean isAllowedCommand(String command) {
        return rights.isAllowedCommand(command);
    }

    public void denyPlayer(String playerName) {
        rights.denyPlayer(playerName);
    }

    public boolean isAllowedGroupName(String groupName) {
        return rights.isAllowedGroupName(groupName);
    }

    public Integer getIntFlagAtt(FlagAtt f) {
        return flagAtts.getIntFlagAtt(f);
    }

    public void setIntFlagAtt(FlagAtt f, Integer i) {
        flagAtts.setIntFlagAtt(f, i);
    }

    public Location getLocFlagAtt(FlagAtt f) {
        return flagAtts.getLocFlagAtt(f);
    }

    public Vector getVectFlagAtt(FlagAtt f) {
        return flagAtts.getVectFlagAtt(f);
    }

    public void setLocFlagAtt(FlagAtt f, Location loc) {
        flagAtts.setLocFlagAtt(f, loc);
    }

    public void setVectFlagAtt(FlagAtt f, Vector v) {
        flagAtts.setVectFlagAtt(f, v);
    }

    public Set<String> getDisallowedCommands() {
        return rights.getDisallowedCommands();
    }

    public Set<String> getAllowedPlayers() {
        return rights.getAllowedPlayers();
    }

    public Set<String> getAllowedGroups() {
        return rights.getAllowedGroups();
    }
}
