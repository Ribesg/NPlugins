package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.common.event.NEventsListener;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncore.node.dodgeball.DodgeBallNode;
import fr.ribesg.bukkit.ncore.node.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.ncore.node.general.GeneralNode;
import fr.ribesg.bukkit.ncore.node.player.PlayerNode;
import fr.ribesg.bukkit.ncore.node.talk.TalkNode;
import fr.ribesg.bukkit.ncore.node.theendagain.TheEndAgainNode;
import fr.ribesg.bukkit.ncore.node.world.WorldNode;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcstats.Metrics;

import java.io.IOException;

/**
 * The Core of the N Plugin Suite
 *
 * @author Ribesg
 */
public class NCore extends JavaPlugin {

	private CuboidNode        cuboidNode;
	private DodgeBallNode     dodgeBallNode;
	private EnchantingEggNode enchantingEggNode;
	private GeneralNode       generalNode;
	private PlayerNode        playerNode;
	private TalkNode          talkNode;
	private TheEndAgainNode   theEndAgainNode;
	private WorldNode         worldNode;

	private Metrics metrics;

	@Override
	public void onEnable() {
		try {
			metrics = new Metrics(this);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new BukkitRunnable() {

			@Override
			public void run() {
				afterNodesLoad();
			}
		}, 5 * 20L /* ~5 seconds */);
	}

	@Override
	public void onDisable() {
		// Nothing yet
	}

	private void afterNodesLoad() {
		boolean noNodeFound = true;
		Metrics.Graph nodesUsedGraph = metrics.createGraph("Nodes used");
		
		/* Cuboid Node */
		if (cuboidNode != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter("Cuboid") {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}
		
		/* DodgeBall Node */
		if (dodgeBallNode != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter("DodgeBall") {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}
		
		/* EnchantingEgg Node */
		if (enchantingEggNode != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter("EnchantingEgg") {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}
		
		/* General Node */
		if (generalNode != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter("General") {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}
		
		/* Player Node */
		if (playerNode != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter("Player") {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}
		
		/* Talk Node */
		if (talkNode != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter("Talk") {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}
		
		/* TheEndAgain Node */
		if (theEndAgainNode != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter("TheEndAgain") {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}
		
		/* World Node */
		if (worldNode != null) {
			nodesUsedGraph.addPlotter(new Metrics.Plotter("World") {

				@Override
				public int getValue() {
					return 1;
				}
			});
			noNodeFound = false;
		}

		metrics.start();

		Bukkit.getPluginManager().registerEvents(new NEventsListener(), this);

		if (noNodeFound) {
			// TODO
		}
	}

	public TalkNode getTalkNode() {
		return talkNode;
	}

	public void setTalkNode(TalkNode talkNode) {
		this.talkNode = talkNode;
	}

	public CuboidNode getCuboidNode() {
		return cuboidNode;
	}

	public void setCuboidNode(CuboidNode cuboidNode) {
		this.cuboidNode = cuboidNode;
	}

	public DodgeBallNode getDodgeBallNode() {
		return dodgeBallNode;
	}

	public void setDodgeBallNode(DodgeBallNode dodgeBallNode) {
		this.dodgeBallNode = dodgeBallNode;
	}

	public EnchantingEggNode getEnchantingEggNode() {
		return enchantingEggNode;
	}

	public void setEnchantingEggNode(EnchantingEggNode enchantingEggNode) {
		this.enchantingEggNode = enchantingEggNode;
	}

	public GeneralNode getGeneralNode() {
		return generalNode;
	}

	public void setGeneralNode(GeneralNode generalNode) {
		this.generalNode = generalNode;
	}

	public PlayerNode getPlayerNode() {
		return playerNode;
	}

	public void setPlayerNode(PlayerNode playerNode) {
		this.playerNode = playerNode;
	}

	public TheEndAgainNode getTheEndAgainNode() {
		return theEndAgainNode;
	}

	public void setTheEndAgainNode(TheEndAgainNode theEndAgainNode) {
		this.theEndAgainNode = theEndAgainNode;
	}

	public WorldNode getWorldNode() {
		return worldNode;
	}

	public void setWorldNode(WorldNode worldNode) {
		this.worldNode = worldNode;
	}
}
