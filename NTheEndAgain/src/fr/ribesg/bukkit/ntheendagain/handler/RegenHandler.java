package fr.ribesg.bukkit.ntheendagain.handler;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ntheendagain.Config;
import fr.ribesg.bukkit.ntheendagain.NTheEndAgain;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/** @author Ribesg */
public class RegenHandler {

    private final EndWorldHandler worldHandler;

    public RegenHandler(EndWorldHandler worldHandler) {
        this.worldHandler = worldHandler;
    }

    public void regen() {
        regen(worldHandler.getConfig().getRegenMethod());
    }

    public void regen(final int type) {
        kickPlayers();
        Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

            @Override
            public void run() {
                switch (type) {
                    case 0:
                        hardRegen();
                        break;
                    case 1:
                        softRegen();
                        break;
                    case 2:
                        crystalRegen();
                        break;
                    default:
                        break;
                }
            }
        }, EndWorldHandler.KICK_TO_REGEN_DELAY);
    }

    public void hardRegenOnStop() {
        hardRegen();
    }

    /*package*/ void regenThenRespawn() {
        regen();
        Bukkit.getScheduler().runTaskLater(worldHandler.getPlugin(), new BukkitRunnable() {

            @Override
            public void run() {
                worldHandler.getRespawnHandler().respawnNoRegen();
            }
        }, EndWorldHandler.REGEN_TO_RESPAWN_DELAY);
    }

    private void hardRegen() {
        final NTheEndAgain plugin = worldHandler.getPlugin();
        final World endWorld = worldHandler.getEndWorld();
        final EndChunks chunks = worldHandler.getChunks();

        plugin.getLogger().info("Regenerating End world \"" + endWorld.getName() + "\"...");
        kickPlayers();
        softRegen();
        for (final EndChunk c : chunks) {
            if (c.hasToBeRegen()) {
                c.cleanCrystalLocations();
                c.resetSavedDragons();
                for (Entity e : endWorld.getChunkAt(c.getX(), c.getZ()).getEntities()) {
                    if (e.getType() == EntityType.ENDER_DRAGON) {
                        worldHandler.getDragons().remove(e.getUniqueId());
                        worldHandler.getLoadedDragons().remove(e.getUniqueId());
                    }
                    e.remove();
                }
                endWorld.regenerateChunk(c.getX(), c.getZ());
                c.setToBeRegen(false);
            }
        }
        plugin.getLogger().info("Done.");
    }

    private void softRegen() {
        worldHandler.getChunks().softRegen();
    }

    private void crystalRegen() {
        worldHandler.getChunks().crystalRegen();
    }

    private void kickPlayers() {
        final Config config = worldHandler.getConfig();
        final NTheEndAgain plugin = worldHandler.getPlugin();
        final World endWorld = worldHandler.getEndWorld();

        switch (config.getRegenAction()) {
            case 0:
                final String[] lines = plugin.getMessages().get(MessageId.theEndAgain_worldRegenerating);
                final StringBuilder messageBuilder = new StringBuilder(lines[0]);
                for (int i = 1; i < lines.length; i++) {
                    messageBuilder.append('\n');
                    messageBuilder.append(lines[i]);
                }
                final String message = messageBuilder.toString();
                for (final Player p : endWorld.getPlayers()) {
                    p.kickPlayer(message);
                }
            case 1:
                for (final Player p : endWorld.getPlayers()) {
                    // TODO Future: Use spawn point defined by NWorld, when NWorld will do it
                    //              and if NWorld is enabled of course
                    p.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
                    plugin.sendMessage(p, MessageId.theEndAgain_worldRegenerating);
                }
            default:
                // Not possible.
                break;
        }
    }
}
