package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * It's more or less like OfflinePlayer for Player
 * It's a Location with the World's name instead of a World Object.
 */
public class NLocation {

    private String worldName;
    private double x;
    private double y;
    private double z;
    private float  yaw;
    private float  pitch;

    public NLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public NLocation(String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0f;
        this.pitch = 0f;
    }

    public NLocation(Location loc) {
        this.worldName = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.yaw = loc.getYaw();
        this.pitch = loc.getPitch();
    }

    public Location toBukkitLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        } else {
            return new Location(world, x, y, z, yaw, pitch);
        }
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public double getX() {
        return x;
    }

    public int getBlockX() {
        return (int) Math.floor(x);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public int getBlockY() {
        return (int) Math.floor(y);
    }

    public void setY(double y) {
        this.y = y;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public double getZ() {
        return z;
    }

    public int getBlockZ() {
        return (int) Math.floor(z);
    }

    public void setZ(double z) {
        this.z = z;
    }
}
