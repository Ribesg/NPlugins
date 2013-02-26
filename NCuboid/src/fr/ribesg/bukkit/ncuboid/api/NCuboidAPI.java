package fr.ribesg.bukkit.ncuboid.api;


import fr.ribesg.bukkit.ncore.nodes.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncuboid.NCuboid;

public class NCuboidAPI extends CuboidNode {
	private final NCuboid	plugin;

	public NCuboidAPI(final NCuboid instance) {
		plugin = instance;
	}
}
