package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.metrics.Metrics;
import fr.ribesg.bukkit.ncore.nodes.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncore.nodes.dodgeball.DodgeBallNode;
import fr.ribesg.bukkit.ncore.nodes.enchantingegg.EnchantingEggNode;
import fr.ribesg.bukkit.ncore.nodes.general.GeneralNode;
import fr.ribesg.bukkit.ncore.nodes.player.PlayerNode;
import fr.ribesg.bukkit.ncore.nodes.talk.TalkNode;
import fr.ribesg.bukkit.ncore.nodes.theendagain.TheEndAgainNode;
import fr.ribesg.bukkit.ncore.nodes.world.WorldNode;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

/**
 * The Core of the N Plugin Suite
 *
 * @author Ribesg
 */
public class NCore extends JavaPlugin {

    private Metrics metrics;

    private CuboidNode        cuboidNode;
    private DodgeBallNode     dodgeBallNode;
    private EnchantingEggNode enchantingEggNode;
    private GeneralNode       generalNode;
    private PlayerNode        playerNode;
    private TalkNode          talkNode;
    private TheEndAgainNode   theEndAgainNode;
    private WorldNode         worldNode;

    @Override
    public void onEnable() {
        try {
            metrics = new Metrics(this);
            Bukkit.getScheduler().runTaskLaterAsynchronously(this, new BukkitRunnable() {

                @Override
                public void run() {
                    enableMetrics();
                }
            }, 5 * 20L /* ~5 seconds */);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Nothing yet
    }

    private void enableMetrics() {
        Metrics.Graph nodesUsedGraph = metrics.createGraph("Nodes used");

        /* Cuboid Node */
        nodesUsedGraph.addPlotter(new Metrics.Plotter("Cuboid") {

            @Override
            public int getValue() {
                return cuboidNode == null ? 0 : 1;
            }
        });

        /* DodgeBall Node */
        nodesUsedGraph.addPlotter(new Metrics.Plotter("DodgeBall") {

            @Override
            public int getValue() {
                return dodgeBallNode == null ? 0 : 1;
            }
        });

        /* EnchantingEgg Node */
        nodesUsedGraph.addPlotter(new Metrics.Plotter("EnchantingEgg") {

            @Override
            public int getValue() {
                return enchantingEggNode == null ? 0 : 1;
            }
        });

        /* General Node */
        nodesUsedGraph.addPlotter(new Metrics.Plotter("General") {

            @Override
            public int getValue() {
                return generalNode == null ? 0 : 1;
            }
        });

        /* Player Node */
        nodesUsedGraph.addPlotter(new Metrics.Plotter("Player") {

            @Override
            public int getValue() {
                return playerNode == null ? 0 : 1;
            }
        });

        /* Talk Node */
        nodesUsedGraph.addPlotter(new Metrics.Plotter("Talk") {

            @Override
            public int getValue() {
                return talkNode == null ? 0 : 1;
            }
        });

        /* TheEndAgain Node */
        nodesUsedGraph.addPlotter(new Metrics.Plotter("TheEndAgain") {

            @Override
            public int getValue() {
                return theEndAgainNode == null ? 0 : 1;
            }
        });

        /* World Node */
        nodesUsedGraph.addPlotter(new Metrics.Plotter("World") {

            @Override
            public int getValue() {
                return worldNode == null ? 0 : 1;
            }
        });

        metrics.start();
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
