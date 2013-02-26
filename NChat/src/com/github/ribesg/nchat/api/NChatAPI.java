package com.github.ribesg.nchat.api;

import com.github.ribesg.nchat.NChat;
import com.github.ribesg.ncore.nodes.chat.ChatNode;

public class NChatAPI extends ChatNode {
	private final NChat	plugin;

	public NChatAPI(final NChat instance) {
		plugin = instance;
	}
}
