/***************************************************************************
 * Project file:    NPlugins - NCuboid - CuboidRegion.java                 *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.CuboidRegion            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class CuboidRegion extends PlayerRegion {

	private NLocation minCorner, maxCorner;
	private int minX, maxX, minY, maxY, minZ, maxZ;

	/**
	 * Create a new Cuboid Region
	 */
	public CuboidRegion(final String cuboidName, final String ownerName, final String worldName, final NLocation minCorner) {

		super(cuboidName, ownerName, worldName, RegionType.CUBOID);

		setMinCorner(minCorner);
		setChunks(null);
	}

	/**
	 * Create a Cuboid Region from a save
	 */
	public CuboidRegion(final String cuboidName, final String ownerName, final String worldName, final RegionState state, final long totalSize, final String welcomeMessage, final String farewellMessage, final Rights rights, final int priority, final Flags flags, final FlagAttributes flagAtts, final NLocation minCorner, final NLocation maxCorner) {

		super(cuboidName, ownerName, worldName, state, totalSize, welcomeMessage, farewellMessage, RegionType.CUBOID, rights, priority, flags, flagAtts);

		setMinCorner(minCorner);
		setMaxCorner(maxCorner);
		setMinX(minCorner.getBlockX());
		setMaxX(maxCorner.getBlockX());
		setMinY(minCorner.getBlockY());
		setMaxY(maxCorner.getBlockY());
		setMinZ(minCorner.getBlockZ());
		setMaxZ(maxCorner.getBlockZ());
		setChunks(computeChunks());
	}

	/**
	 * Called when the player select the second corner
	 */
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
			setState(RegionState.TMPSTATE2);
			setTotalSize((getMaxX() - getMinX() + 1) * (getMaxY() - getMinY() + 1) * (getMaxZ() - getMinZ() + 1));
			setChunks(computeChunks());
		}
	}

	/**
	 * @see CuboidRegion#create(String)
	 */
	@Override
	public void create(final String regionName) {
		super.create(regionName);
	}

	/**
	 * Should only be used when the cuboid is not in the byChunks map
	 */
	public Set<ChunkCoord> computeChunks() {
		final Set<ChunkCoord> chunks = new HashSet<>();
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

	// Check if <x,y,z> is in a Region
	@Override
	public boolean contains(final double x, final double y, final double z) {
		return minX <= x && maxX + 1 > x && minZ <= z && maxZ + 1 > z && minY <= y && maxY + 1 > y;
	}

	@Override
	public boolean overlaps(final PlayerRegion r) {
		switch (r.getType()) {
			case CUBOID:
				final CuboidRegion o = (CuboidRegion) r;
				return !((this.minX > o.maxX || this.maxX < o.minX) ||
				         (this.minY > o.maxY || this.maxY < o.minY) ||
				         (this.minZ > o.maxZ || this.maxZ < o.minZ));
			default:
				throw new UnsupportedOperationException("Note yet implemented for " + r.getType().name());
		}
	}

	@Override
	public String getSizeString() {
		return (maxX - minX + 1) + "x" + (maxY - minY + 1) + "x" + (maxZ - minZ + 1);
	}

	public NLocation getMaxCorner() {
		return maxCorner;
	}

	public void setMaxCorner(final NLocation maxCorner) {
		this.maxCorner = maxCorner;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(final int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(final int maxY) {
		this.maxY = maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public void setMaxZ(final int maxZ) {
		this.maxZ = maxZ;
	}

	public NLocation getMinCorner() {
		return minCorner;
	}

	public void setMinCorner(final NLocation minCorner) {
		this.minCorner = minCorner;
	}

	public int getMinX() {
		return minX;
	}

	public void setMinX(final int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(final int minY) {
		this.minY = minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public void setMinZ(final int minZ) {
		this.minZ = minZ;
	}

	public long getMaxLength() {
		final long xLength = getMaxX() - getMinX();
		final long yLength = getMaxY() - getMinY();
		final long zLength = getMaxZ() - getMinZ();
		return Math.max(xLength, Math.max(yLength, zLength));
	}
}
