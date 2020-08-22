package io.github.fabriccommunity.events.play;

import java.util.concurrent.atomic.AtomicInteger;

import io.github.fabriccommunity.events.util.WrappedFloat;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

/**
 * A collection of events for player interaction.
 */
public final class PlayerInteractionEvents {
	/**
	 * An event which runs when the player picks up experience.
	 */
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
	 * Event that runs when food level is added to a hunger manager.
	 */
	public static final Event<RestoreHunger> RESTORE_HUNGER = EventFactory.createArrayBacked(RestoreHunger.class, listeners -> (manager, original, saturation) -> {
		for (RestoreHunger listener : listeners) {
			// karen would like to speak to the hunger manager by the way.
			ActionResult result = listener.onRestoreHunger(manager, original, saturation);

			if (result != ActionResult.PASS) {
				return result;
			}
		}

		return ActionResult.PASS;
	});

	/**
	 * Event that runs when food level is added to a hunger manager.
	 */
	public static final Event<ConsumeStack> CONSUME_STACK = EventFactory.createArrayBacked(ConsumeStack.class, listeners -> (manager, stack, originalHunger, originalSaturation, hunger, saturation) -> {
		for (ConsumeStack listener : listeners) {
			ActionResult result = listener.onRestoreSaturation(manager, stack, originalHunger, originalSaturation, hunger, saturation);

			if (result != ActionResult.PASS) {
				return result;
			}
		}

		return ActionResult.PASS;
	});

	/**
	 * Event that runs when saturation is added to a hunger manager.
	 */
	public static final Event<RestoreSaturation> RESTORE_SATURATION = EventFactory.createArrayBacked(RestoreSaturation.class, listeners -> (manager, original, saturation) -> {
		for (RestoreSaturation listener : listeners) {
			ActionResult result = listener.onRestoreSaturation(manager, original, saturation);

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
		 * <ul>
		 * <li>{@code SUCCESS} or {@code CONSUME} to cancel further event processing and add the experience in {@code experience}.<br/>
		 * <li>{@code PASS} pass event handling on to further processing. If all listeners pass, it is treated as a {@code SUCCESS}.
		 * <li>{@code FAIL} to cancel further event processing and add the original experience.
		 * </ul>
		 */
		ActionResult onXPGained(PlayerEntity player, final int original, AtomicInteger experience);
	}

	/**
	 * Event that runs when an item is consumed in the hunger manager. In the vanilla game, only the player has a hunger manager.
	 * @author Valoeghese
	 * @apiNote this runs before {@link RestoreSaturation} and {@link RestoreHunger}.
	 */
	@FunctionalInterface
	public interface ConsumeStack {
		/**
		 * @param hungerManager the hunger manager.
		 * @param stack the stack of the item to be consumed.
		 * @param originalHunger the original hunger that was going to be added to the hunger manager.
		 * @param originalSaturation the original saturation that was going to be added to the hunger manager.
		 * @param hunger the hunger to be added to the hunger manager. If the value contained in this does not equal {@code originalHunger}, the value has been modded.
		 * @param saturation the saturation to be added to the hunger manager. If the value contained in this does not equal {@code originalSaturation}, the value has been modded.
		 * @return
		 * <ul>
		 * <li>{@code SUCCESS} or {@code CONSUME} to cancel further event processing and add the hunger in {@code hunger} and saturation in {@code saturation}.<br/>
		 * <li>{@code PASS} pass event handling on to further processing. If all listeners pass, it is treated as a {@code SUCCESS}.
		 * <li>{@code FAIL} to cancel further event processing and not consume the stack.
		 * </ul>
		 */
		ActionResult onRestoreSaturation(HungerManager hungerManager, ItemStack stack, final int originalHunger, final float originalSaturation, AtomicInteger hunger, WrappedFloat saturation);
	}

	/**
	 * Event that runs when food level is added to a hunger manager. In the vanilla game, only the player has a hunger manager.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface RestoreHunger {
		/**
		 * @param hungerManager the hunger manager.
		 * @param original the original food level that was going to be added to the hunger manager.
		 * @param food the food level to be added to the hunger manager. If the value contained in this does not equal {@code original}, the value has been modded.
		 * @return
		 * <ul>
		 * <li>{@code SUCCESS} or {@code CONSUME} to cancel further event processing and add the food level in {@code hunger}.<br/>
		 * <li>{@code PASS} pass event handling on to further processing. If all listeners pass, it is treated as a {@code SUCCESS}.
		 * <li>{@code FAIL} to cancel further event processing and add the original hunger level.
		 * </ul>
		 */
		ActionResult onRestoreHunger(HungerManager hungerManager, final int original, AtomicInteger hunger);
	}

	/**
	 * Event that runs when saturation is added to a hunger manager. In the vanilla game, only the player has a hunger manager.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface RestoreSaturation {
		/**
		 * @param hungerManager the hunger manager.
		 * @param original the original saturation that was going to be added to the hunger manager.
		 * @param saturation the saturation to be added to the hunger manager. If the value contained in this does not equal {@code original}, the value has been modded.
		 * @return
		 * <ul>
		 * <li>{@code SUCCESS} or {@code CONSUME} to cancel further event processing and add the saturation in {@code saturation}.<br/>
		 * <li>{@code PASS} pass event handling on to further processing. If all listeners pass, it is treated as a {@code SUCCESS}.
		 * <li>{@code FAIL} to cancel further event processing and add the original saturation.
		 * </ul>
		 */
		ActionResult onRestoreSaturation(HungerManager hungerManager, final float original, WrappedFloat saturation);
	}
}
