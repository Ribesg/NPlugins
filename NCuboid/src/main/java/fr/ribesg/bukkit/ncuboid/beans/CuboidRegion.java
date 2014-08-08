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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;

public class CuboidRegion extends PlayerRegion {

	private NLocation minCorner, maxCorner;
	private int minX, maxX, minY, maxY, minZ, maxZ;

	/**
	 * Create a new Cuboid Region
	 */
	public CuboidRegion(final String cuboidName, final UUID ownerId, final String worldName, final NLocation minCorner) {

		super(cuboidName, ownerId, worldName, RegionType.CUBOID);

		this.setMinCorner(minCorner);
		this.setChunks(null);
	}

	/**
	 * Create a Cuboid Region from a save
	 */
	public CuboidRegion(final String cuboidName, final UUID ownerId, final String worldName, final RegionState state, final long totalSize, final Rights rights, final int priority, final Flags flags, final Attributes flagAtts, final NLocation minCorner, final NLocation maxCorner) {

		super(cuboidName, ownerId, worldName, state, totalSize, RegionType.CUBOID, rights, priority, flags, flagAtts);

		this.setMinCorner(minCorner);
		this.setMaxCorner(maxCorner);
		this.setMinX(minCorner.getBlockX());
		this.setMaxX(maxCorner.getBlockX());
		this.setMinY(minCorner.getBlockY());
		this.setMaxY(maxCorner.getBlockY());
		this.setMinZ(minCorner.getBlockZ());
		this.setMaxZ(maxCorner.getBlockZ());
		this.setChunks(this.computeChunks());
	}

	/**
	 * Called when the player select the second corner
	 */
	public void secondPoint(final Location secondPoint) {
		if (secondPoint.getWorld().getName().equals(this.getWorldName())) {
			this.setMinX(this.minCorner.getBlockX() < secondPoint.getBlockX() ? this.minCorner.getBlockX() : secondPoint.getBlockX());
			this.setMinY(this.minCorner.getBlockY() < secondPoint.getBlockY() ? this.minCorner.getBlockY() : secondPoint.getBlockY());
			this.setMinZ(this.minCorner.getBlockZ() < secondPoint.getBlockZ() ? this.minCorner.getBlockZ() : secondPoint.getBlockZ());
			this.setMaxX(this.minX == secondPoint.getBlockX() ? this.minCorner.getBlockX() : secondPoint.getBlockX());
			this.setMaxY(this.minY == secondPoint.getBlockY() ? this.minCorner.getBlockY() : secondPoint.getBlockY());
			this.setMaxZ(this.minZ == secondPoint.getBlockZ() ? this.minCorner.getBlockZ() : secondPoint.getBlockZ());
			this.setMinCorner(new NLocation(this.getWorldName(), this.minX, this.minY, this.minZ));
			this.setMaxCorner(new NLocation(this.getWorldName(), this.maxX, this.maxY, this.maxZ));
			this.setState(RegionState.TMPSTATE2);
			this.setTotalSize((this.maxX - this.minX + 1) * (this.maxY - this.minY + 1) * (this.maxZ - this.minZ + 1));
			this.setChunks(this.computeChunks());
		}
	}

	@Override
	public void create(final String regionName) {
		super.create(regionName);
	}

	/**
	 * Should only be used when the cuboid is not in the byChunks map
	 */
	public Set<ChunkCoord> computeChunks() {
		final Set<ChunkCoord> chunks = new HashSet<>();
		final ChunkCoord cMin = new ChunkCoord(this.minCorner);
		final ChunkCoord cMax = new ChunkCoord(this.maxCorner);
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
		return this.minX <= x && this.maxX + 1 > x && this.minZ <= z && this.maxZ + 1 > z && this.minY <= y && this.maxY + 1 > y;
	}

	@Override
	public boolean overlaps(final PlayerRegion r) {
		switch (r.getType()) {
			case CUBOID:
				final CuboidRegion o = (CuboidRegion)r;
				return !(this.minX > o.maxX || this.maxX < o.minX ||
				         this.minY > o.maxY || this.maxY < o.minY ||
				         this.minZ > o.maxZ || this.maxZ < o.minZ);
			default:
				throw new UnsupportedOperationException("Note yet implemented for " + r.getType().name());
		}
	}

	@Override
	public String getSizeString() {
		return (this.maxX - this.minX + 1) + "x" + (this.maxY - this.minY + 1) + 'x' + (this.maxZ - this.minZ + 1);
	}

	public NLocation getMaxCorner() {
		return this.maxCorner;
	}

	public void setMaxCorner(final NLocation maxCorner) {
		this.maxCorner = maxCorner;
	}

	public int getMaxX() {
		return this.maxX;
	}

	public void setMaxX(final int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return this.maxY;
	}

	public void setMaxY(final int maxY) {
		this.maxY = maxY;
	}

	public int getMaxZ() {
		return this.maxZ;
	}

	public void setMaxZ(final int maxZ) {
		this.maxZ = maxZ;
	}

	public NLocation getMinCorner() {
		return this.minCorner;
	}

	public void setMinCorner(final NLocation minCorner) {
		this.minCorner = minCorner;
	}

	public int getMinX() {
		return this.minX;
	}

	public void setMinX(final int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return this.minY;
	}

	public void setMinY(final int minY) {
		this.minY = minY;
	}

	public int getMinZ() {
		return this.minZ;
	}

	public void setMinZ(final int minZ) {
		this.minZ = minZ;
	}

	public long getMaxLength() {
		final long xLength = this.maxX - this.minX;
		final long yLength = this.maxY - this.minY;
		final long zLength = this.maxZ - this.minZ;
		return Math.max(xLength, Math.max(yLength, zLength));
	}
}
