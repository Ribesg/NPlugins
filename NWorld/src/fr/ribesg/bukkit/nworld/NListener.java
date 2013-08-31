package fr.ribesg.bukkit.nworld;

import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.TravelAgent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

import java.util.logging.Logger;

/** @author Ribesg */
public class NListener implements Listener {

	private static final Logger LOG = Logger.getLogger(NListener.class.getName());

	private final NWorld plugin;

	public NListener(final NWorld instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onEntityUsePortal(final EntityPortalEvent event) {
		// TODO Debug this event to understand how it works
		LOG.info("########## STARTING LOGGING ENTITY PORTAL EVENT ##########");
		LOG.info("A " + event.getEntityType().toString().toLowerCase() + " is trying to teleport.");
		LOG.info("Its location is " + NLocation.toString(event.getEntity().getLocation()));
		LOG.info("The from location is " + NLocation.toString(event.getFrom()));
		LOG.info("The to location is " + NLocation.toString(event.getTo()));
		LOG.info("The 'UseTravelAgent' flag is set to " + event.useTravelAgent());
		final TravelAgent agent = event.getPortalTravelAgent();
		LOG.info("The Travel Agent is " + (agent == null ? "" : "not ") + "null");
		if (agent != null) {
			LOG.info("The Travel Agent " + (agent.getCanCreatePortal() ? "can" : "can't") + " create portal");
			LOG.info("It has a search radius of " + agent.getSearchRadius() + " and a creation radius of " + agent.getCreationRadius());
		}
		LOG.info("This event is " + (event.isCancelled() ? "" : "not ") + "cancelled!");
		LOG.info("Nothing else to say!");
		LOG.info("########## END OF LOGGING ENTITY PORTAL EVENT ##########");
	}
}
