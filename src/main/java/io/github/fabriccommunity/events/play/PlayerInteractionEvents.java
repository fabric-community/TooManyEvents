package io.github.fabriccommunity.events.play;

import java.util.concurrent.atomic.AtomicInteger;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

/**
 * A collection of events for player interaction.
 */
public final class PlayerInteractionEvents {
	public static final Event<XPGain> XP_GAIN = EventFactory.createArrayBacked(XPGain.class, listeners -> (player, original, experience) -> {
		for (XPGain listener : listeners) {
			ActionResult result = listener.onXPGained(player, original, experience);

			if (result != ActionResult.PASS) {
				return result;
			}
		}

		return ActionResult.PASS;
	});

	/**
	 * An event which runs when the player picks up experience.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface XPGain {
		/**
		 * @param player the player gaining the experience.
		 * @param original the original experience to be gained.
		 * @param experience the experience which will be gained. If the value stored in this is different to {@code original}, a mod has modified the experience gained.
		 * @return
		 * {@code SUCCESS} or {@code CONSUME} to cancel further event processing and add the experinece in {@code experience}.
		 * {@code PASS} pass event handling on to further processing. If all listeners pass, it is treated as a {@code SUCCESS}.
		 * {@code FAIL} to cancel further event processing and add the original experience.
		 */
		ActionResult onXPGained(PlayerEntity player, final int original, AtomicInteger experience);
	}
}
