package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.play.PlayerInteractionEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.ActionResult;

public class PlayerInteractionTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			PlayerInteractionEvents.XP_GAIN.register((player, original, xp) -> {
				xp.set(100 * xp.get());
				return ActionResult.PASS;
			});
		}
	}
}
