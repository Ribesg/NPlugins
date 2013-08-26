package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.utils.ChunkCoord;
import fr.ribesg.bukkit.ncore.utils.NLocation;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class RectCuboid extends PlayerCuboid {

	private NLocation minCorner, maxCorner;
	private int minX, maxX, minY, maxY, minZ, maxZ;

	// Create a new Rectangular Cuboid
	public RectCuboid(final String cuboidName, final String ownerName, final String worldName, final NLocation minCorner) {

		super(cuboidName, ownerName, worldName, CuboidType.RECT);

		setMinCorner(minCorner);
		setChunks(null);
	}

	// Create a Rectangular Cuboid from a save
	public RectCuboid(final String cuboidName,
	                  final String ownerName,
	                  final String worldName,
	                  final CuboidState state,
	                  final long totalSize,
	                  final String welcomeMessage,
	                  final String farewellMessage,
	                  final Set<ChunkCoord> chunks,
	                  final Rights rights,
	                  final int priority,
	                  final Flags flags,
	                  final FlagAttributes flagAtts,
	                  final NLocation minCorner,
	                  final NLocation maxCorner) {

		super(cuboidName,
		      ownerName,
		      worldName,
		      state,
		      totalSize,
		      welcomeMessage,
		      farewellMessage,
		      chunks,
		      CuboidType.RECT,
		      rights,
		      priority,
		      flags,
		      flagAtts);

		setMinCorner(minCorner);
		setMaxCorner(maxCorner);
		setMinX(minCorner.getBlockX());
		setMaxX(maxCorner.getBlockX());
		setMinY(minCorner.getBlockY());
		setMaxY(maxCorner.getBlockY());
		setMinZ(minCorner.getBlockZ());
		setMaxZ(maxCorner.getBlockZ());
	}

	// The player select the second corner
	public void secondPoint(final Location secondPoint) {
		if (secondPoint.getWorld().getName().equals(getWorldName())) {
			setMinX(getMinCorner().getBlockX() < secondPoint.getBlockX() ? getMinCorner().getBlockX() : secondPoint.getBlockX());
			setMinY(getMinCorner().getBlockY() < secondPoint.getBlockY() ? getMinCorner().getBlockY() : secondPoint.getBlockY());
			setMinZ(getMinCorner().getBlockZ() < secondPoint.getBlockZ() ? getMinCorner().getBlockZ() : secondPoint.getBlockZ());
			setMaxX(getMinX() == secondPoint.getBlockX() ? getMinCorner().getBlockX() : secondPoint.getBlockX());
			setMaxY(getMinY() == secondPoint.getBlockY() ? getMinCorner().getBlockY() : secondPoint.getBlockY());
			setMaxZ(getMinZ() == secondPoint.getBlockZ() ? getMinCorner().getBlockZ() : secondPoint.getBlockZ());
			setMinCorner(new NLocation(getWorldName(), getMinX(), getMinY(), getMinZ()));
			setMaxCorner(new NLocation(getWorldName(), getMaxX(), getMaxY(), getMaxZ()));
			setState(CuboidState.TMPSTATE2);
		}
	}

	@Override
	public void create(final String cuboidName) {
		super.create(cuboidName);
		setTotalSize((getMaxX() - getMinX() + 1) * (getMaxY() - getMinY() + 1) * (getMaxZ() - getMinZ() + 1));
		setChunks(computeChunks());
	}

	// Should only be used when the cuboid is not in the byChunks map
	public Set<ChunkCoord> computeChunks() {
		final Set<ChunkCoord> chunks = new HashSet<ChunkCoord>();
		final ChunkCoord cMin = new ChunkCoord(getMinCorner());
		final ChunkCoord cMax = new ChunkCoord(getMaxCorner());
		for (int x = cMin.getX(); x <= cMax.getX(); x++) {
			for (int z = cMin.getZ(); z <= cMax.getZ(); z++) {
				final ChunkCoord newChunk = new ChunkCoord(x, z, cMin.getWorldName());
				chunks.add(newChunk);
			}
		}
		return chunks;
	}

	// Check if <x,y,z> is in a Cuboid
	@Override
	public boolean contains(final double x, final double y, final double z) {
		// Do not use getters here to be faster
		return minX <= x && maxX + 1 > x && minY <= y && maxY + 1 > y && minZ <= z && maxZ + 1 > z;
	}

	@Override
	public String getSizeString() {
		return maxX - minX + 1 + "x" + (maxY - minY + 1) + "x" + (maxZ - minZ + 1);
	}

	public NLocation getMaxCorner() {
		return maxCorner;
	}

	public void setMaxCorner(NLocation maxCorner) {
		this.maxCorner = maxCorner;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public void setMaxZ(int maxZ) {
		this.maxZ = maxZ;
	}

	public NLocation getMinCorner() {
		return minCorner;
	}

	public void setMinCorner(NLocation minCorner) {
		this.minCorner = minCorner;
	}

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public void setMinZ(int minZ) {
		this.minZ = minZ;
	}
}
