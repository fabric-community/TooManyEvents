package io.github.fabriccommunity.events.play;

import java.util.concurrent.atomic.AtomicReference;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

/**
 * Collection of events pertaining to items and item stacks in gameplay.
 */
public final class ItemEvents {
	/**
	 * An event which runs when an entity tries to eat food.
	 */
	public static final Event<EatFood> EAT_FOOD = EventFactory.createArrayBacked(EatFood.class, listeners -> (entity, world, original, eaten, result) -> {
		for (EatFood listener : listeners) {
			ActionResult eventResult = listener.onEatFood(entity, world, original, eaten, result);

			if (eventResult != ActionResult.PASS) {
				return eventResult;
			}
		}

		return ActionResult.PASS;
	});

	/**
	 * Called when a block item is placed. Is configured to work server side.
	 */
	public static final Event<Placed> PLACED = EventFactory.createArrayBacked(Placed.class, listeners -> (context, original, state) -> {
		for (Placed listener : listeners) {
			ActionResult eventResult = listener.onBlockPlaced(context, original, state);

			if (eventResult != ActionResult.PASS) {
				return eventResult;
			}
		}

		return ActionResult.PASS;
	});

	/**
	 * Called when an entity tries to eat an item.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface EatFood {
		/**
		 * @param entity the entity eating the food.
		 * @param world the world the entity is eating the food in.
		 * @param original the original stack of food to be eaten.
		 * @param eaten the stack of food which is to be eaten. If the value stored herein differs from {@code original}, the value has been modded.<br/>
		 * @param result {@code null} by default. if {@code null}, the stack returned from eating will be the result of the eat method.
		 * If this is altered the resulting stack will be the item stored in here, regardless of whether the event fails.<br/>
		 * @return
		 * <ul>
		 * <li>{@code SUCCESS} or {@code CONSUME} to cancel further event processing and eat the food in {@code stack}<br/>
		 * <li>{@code PASS} pass event handling on to further processing. If all listeners pass, it is treated as a {@code SUCCESS}.
		 * <li>{@code FAIL} to cancel further event processing and do not eat the food.
		 * </ul>
		 */
		ActionResult onEatFood(LivingEntity entity, World world, final ItemStack original, AtomicReference<ItemStack> eaten, AtomicReference<ItemStack> result);
	}

	/**
	 * Called when a block item is placed. Is configured to work server side.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface Placed {
		/**
		 * @param context the context of the block item placement.
		 * @param original the original blockstate to be placed.
		 * @param state the state to be placed. If the value stored herein differs from {@code original}, another mod has modified it.
		 * @return
		 * <ul>
		 * <li>{@code SUCCESS} to cancel further event processing place the block state in {@code state}<br/>
		 * <li>{@code CONSUME} to cancel further event processing and fall back to vanilla behaviour (not modifying the block).<br/>
		 * <li>{@code PASS} pass event handling on to further processing. If all listeners pass, it is treated as a {@code SUCCESS} if the block state has been modified from the original, and treated as a {@code CONSUME} if the block state has not been modified from the original.
		 * <li>{@code FAIL} to cancel further event processing and not place the block.
		 * </ul>
		 */
		ActionResult onBlockPlaced(ItemPlacementContext context, final BlockState original, AtomicReference<BlockState> state);
	}
}
