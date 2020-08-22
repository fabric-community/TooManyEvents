package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.play.ItemEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class EatFoodTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			// This code makes potatoes behave as if they were golden apples.
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
		}
	}
}
