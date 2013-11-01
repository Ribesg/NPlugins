package fr.ribesg.bukkit.ncore.common.event;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * This event will be thrown once the Player has successfully joined the
 * server i.e. AFTER PlayerJoinEvent.
 * It should be used to send Welcome (and other) messages to the Player.
 */
public class PlayerJoinedEvent extends PlayerEvent {

	// Handlers
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public PlayerJoinedEvent(final Player who) {
		super(who);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
