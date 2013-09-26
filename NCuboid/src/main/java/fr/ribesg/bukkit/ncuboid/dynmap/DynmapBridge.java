package fr.ribesg.bukkit.ncuboid.dynmap;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import fr.ribesg.bukkit.ncuboid.beans.CuboidRegion;
import fr.ribesg.bukkit.ncuboid.beans.GeneralRegion;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.util.Random;
import java.util.logging.Logger;

public class DynmapBridge {

	private static final Logger LOG = Logger.getLogger(DynmapBridge.class.getName());

	private static final String DYNMAP_PLUGIN_NAME = "dynmap";
	private static final String MARKERSET_ID       = "ncuboids";
	private static final String MARKERSET_NAME     = "Regions";

	private static final Random rand = new Random();

	private final MarkerSet markerSet;

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
				LOG.warning("Failed to initialize DynmapBridge!");
			}
		}
		this.markerSet = markerSet;
	}

	public void initialize(final Iterable<GeneralRegion> regions) {
		if (this.markerSet != null) {
			for (final GeneralRegion r : regions) {
				if (r.isDynmapable() && r.isShownOnDynmap()) {
					this.handle(r);
				}
			}
		}
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
		if (region.isShownOnDynmap()) {
			return show(region);
		} else {
			return hide(region);
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
					LOG.severe("Unable to dynmap a region of type '" + region.getType() + "'!");
					return false;
			}
		}
	}

	public boolean hide(final GeneralRegion region) {
		if (this.markerSet == null || !region.isDynmapable()) {
			return false;
		} else {
			final String id = StringUtils.toLowerCamelCase(region.getRegionName());
			final Marker marker = this.markerSet.findMarker(id);
			if (marker == null) {
				return false;
			} else {
				marker.deleteMarker();
				return true;
			}
		}
	}

	/** Precondition: We already checked that this cuboid region can be shown and has to be shown. */
	private boolean showCuboidRegion(final CuboidRegion region) {
		// Parameter 1: marker ID
		final String id = StringUtils.toLowerCamelCase(region.getRegionName());

		// Parameter 2: marker label
		final String lbl = "<strong>" + region.getRegionName() + "</strong>";

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
			LOG.warning("Failed to create marker for region " + region.getRegionName());
			return false;
		}

		// Then set the Y!
		marker.setRangeY(region.getMinY(), region.getMaxY());

		// Description
		final StringBuilder description = new StringBuilder();
		description.append("<ul>");
		description.append("<li>Hello</li>");
		description.append("<li>World</li>");
		description.append("</ul>");
		marker.setDescription(description.toString());

		// Random pastel color!
		final int red = 128 + rand.nextInt(128);
		final int green = 128 + rand.nextInt(128);
		final int blue = 128 + rand.nextInt(128);
		final int color = Color.fromRGB(red, green, blue).asRGB();
		marker.setLineStyle(1, 0.9, color);
		marker.setFillStyle(0.1, color);

		return true;
	}
}
