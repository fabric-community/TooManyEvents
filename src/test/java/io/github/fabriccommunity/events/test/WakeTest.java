package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.play.PlayerInteractionEvents;
import net.fabricmc.api.ModInitializer;

public class WakeTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			PlayerInteractionEvents.WAKE_UP.register((player, updateSleepingPlayers, sleepT20) -> {
				if (!player.world.isClient() && player.getHungerManager().getFoodLevel() < 16) {
					player.setHealth(player.getHealth() - 1);
				}
			});
		}
	}
}
