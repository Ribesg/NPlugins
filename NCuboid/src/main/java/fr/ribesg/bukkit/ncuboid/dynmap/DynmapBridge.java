/***************************************************************************
 * Project file:    NPlugins - NCuboid - DynmapBridge.java                 *
 * Full Class name: fr.ribesg.bukkit.ncuboid.dynmap.DynmapBridge           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.dynmap;
import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.ncuboid.beans.CuboidRegion;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.util.logging.Logger;

public class DynmapBridge {

	private static final Logger LOGGER = Logger.getLogger(DynmapBridge.class.getName());

	private static final String DYNMAP_PLUGIN_NAME = "dynmap";
	private static final String MARKERSET_ID       = "ncuboids";
	private static final String MARKERSET_NAME     = "Regions";

	private final MarkerSet markerSet;

	private boolean initialized;

	public DynmapBridge() {
		final Plugin dynmapPlugin = Bukkit.getPluginManager().getPlugin(DYNMAP_PLUGIN_NAME);
		MarkerSet markerSet = null;
		if (dynmapPlugin != null) {
			final DynmapCommonAPI api = (DynmapCommonAPI) dynmapPlugin;
			final MarkerAPI markerApi = api.getMarkerAPI();
			markerSet = markerApi.getMarkerSet(MARKERSET_ID);
			if (markerSet == null) {
				markerSet = markerApi.createMarkerSet(MARKERSET_ID, MARKERSET_NAME, null, false);
			}
			if (markerSet == null) {
				LOGGER.warning("Failed to initialize DynmapBridge!");
			}
		}
		this.markerSet = markerSet;
		this.initialized = false;
	}

	public void initialize(final Iterable<GeneralRegion> regions) {
		if (this.markerSet != null) {
			for (final GeneralRegion r : regions) {
				this.handle(r);
			}
		}
		this.initialized = true;
	}

	public void reinitialize(final Iterable<GeneralRegion> regions) {
		if (this.markerSet != null) {
			for (final Marker marker : this.markerSet.getMarkers()) {
				marker.deleteMarker();
			}
			this.initialize(regions);
		}
	}

	public boolean handle(final GeneralRegion region) {
		if (region.isDynmapable()) {
			if (region.getFlag(Flag.HIDDEN)) {
				return hide(region);
			} else {
				return show(region);
			}
		} else {
			return false;
		}
	}

	public boolean show(final GeneralRegion region) {
		if (this.markerSet == null || !region.isDynmapable()) {
			return false;
		} else {
			switch (region.getType()) {
				case CUBOID:
					return showCuboidRegion((CuboidRegion) region);
				default:
					LOGGER.severe("Unable to dynmapize a region of type '" + region.getType() + "'!");
					return false;
			}
		}
	}

	public boolean hide(final GeneralRegion region) {
		if (this.markerSet == null || !region.isDynmapable()) {
			return false;
		} else {
			// TODO Handle non-area marker if needed
			final String id = StringUtil.toLowerCamelCase(region.getRegionName());
			final AreaMarker marker = this.markerSet.findAreaMarker(id);
			if (marker == null) {
				return false;
			} else {
				marker.deleteMarker();
				return true;
			}
		}
	}

	/**
	 * Precondition: We already checked that this cuboid region can be shown and has to be shown.
	 */
	private boolean showCuboidRegion(final CuboidRegion region) {
		// Parameter 1: marker ID
		final String id = StringUtil.toLowerCamelCase(region.getRegionName());

		// Parameter 2: marker label
		final String lbl = region.getRegionName();

		// Parameter 3: if label contains HTML, always true
		final boolean markup = true;

		// Parameter 4: world id, use world name? FIXME
		final String world = region.getWorldName();

		// Parameter 5 & 6: points coords
		//
		// z                    max
		// ^     3 - - - - - - 2
		// |     |             |
		// |     |             |
		// |     |             |
		// |     0 - - - - - - 1
		// |  min
		// + - - - - - - - - - - - > x
		//
		final double[] x = new double[4];
		final double[] z = new double[4];
		x[0] = x[1] = region.getMinX();
		x[2] = x[3] = region.getMaxX();
		z[0] = z[3] = region.getMinZ();
		z[1] = z[2] = region.getMaxZ();

		// Parameter 7: if marker is persistent. It's not.
		final boolean persistent = false;

		// Do the actual stuff!
		final AreaMarker marker = this.markerSet.createAreaMarker(id, lbl, markup, world, x, z, persistent);

		if (marker == null) {
			LOGGER.warning("Failed to create marker for region " + region.getRegionName());
			return false;
		}

		// Then set the Y!
		marker.setRangeY(region.getMinY(), region.getMaxY());

		// Description
		final String wrap = "<br />";
		final StringBuilder description = new StringBuilder();
		description.append("<strong>").append(lbl).append("</strong>").append(wrap);
		description.append("Owner: ").append(UuidDb.getName(region.getOwnerId())).append(wrap);
		description.append("Size: ").append(region.getTotalSize()).append(" blocks").append(wrap);
		description.append("Flags: ");
		description.append("<ul>");
		for (final Flag f : Flag.values()) {
			if (region.getFlag(f)) {
				description.append("  <li>").append(f.toString()).append("</li>");
			}
		}
		description.append("<ul>");
		marker.setDescription(description.toString());

		// Color!
		final int color = Color.fromRGB(200, 200, 20).asRGB();
		marker.setLineStyle(1, 0.9, color);
		marker.setFillStyle(0.1, color);

		return true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(final boolean initialized) {
		this.initialized = initialized;
	}
}
