package io.github.fabriccommunity.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class TooManyEvents implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("TooManyEvents");

	@Override
	public void onInitialize() {
		LOGGER.info("Haha events go brrrr");
	}
}
