package com.github.ribesg.nplayer.api;

import com.github.ribesg.ncore.nodes.punisher.PunisherNode;
import com.github.ribesg.nplayer.NPlayer;

public class NPlayerAPI extends PunisherNode {

	private final NPlayer	plugin;

	public NPlayerAPI(final NPlayer instance) {
		plugin = instance;
	}

}
