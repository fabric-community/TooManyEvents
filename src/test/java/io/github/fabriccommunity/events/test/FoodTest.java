package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.play.ItemEvents;
import io.github.fabriccommunity.events.play.PlayerInteractionEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class FoodTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			// makes potatoes behave as if they were golden apples.
			ItemEvents.EAT_FOOD.register((entity, world, original, eaten, result) -> {
				if (original.getItem() == Items.POTATO) {
					eaten.set(new ItemStack(Items.GOLDEN_APPLE)); // use golden apple eat effects
					// Make it return what it would be if the original had been eaten.
					// Otherwise it will return nothing due to eating a stack of 1 golden apples giving back 0 golden apples, which is nothing.
					ItemStack resultstk = original.copy();
					resultstk.decrement(1);
					result.set(resultstk); //
					return ActionResult.SUCCESS;
				}

				return ActionResult.PASS;
			});

			// makes baked potatoes heal 20 hunger
			PlayerInteractionEvents.CONSUME_STACK.register((hungerManager, stack, originalHunger, originalSaturation, hunger, saturation) -> {
				if (stack.getItem() == Items.BAKED_POTATO) {
					hunger.set(20);
					return ActionResult.SUCCESS;
				}

				return ActionResult.PASS;
			});

			// make all things heal 5x more saturation if hunger < 5
			// but pass stuff on to further events in case other mods want to mess with saturation
			PlayerInteractionEvents.RESTORE_SATURATION.register((manager, original, saturation) -> {
				if (manager.getFoodLevel() < 5) {
					saturation.set(saturation.get() * 5);
				}

				return ActionResult.PASS;
			});
		}
	}
}
