/***************************************************************************
 * Project file:    NPlugins - NCore - ChunkRegenEvent.java                *
 * Full Class name: fr.ribesg.bukkit.ncore.event.theendagain.ChunkRegenEvent
 *                                                                         *
 *                Copyright (c) 2014 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.event.theendagain;
import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called by the NTheEndAgain node just before regenerating
 * a chunk. This is only called if the chunk is not protected and if the chunk
 * WILL be regen.
 * The cancellation of this event will prevent the regeneration, and the chunk
 * will no longer be flagged as to-be-regen until the next world regeneration.
 */
public class ChunkRegenEvent extends Event implements Cancellable {

	// Handlers
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private final Chunk   chunk;
	private       boolean cancelled;

	public ChunkRegenEvent(final Chunk chunk) {
		this.chunk = chunk;
		this.cancelled = false;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	/** The chunk that is about to be regen (or not) */
	public Chunk getChunk() {
		return chunk;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}
}
