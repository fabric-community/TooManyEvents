package io.github.fabriccommunity.events.impl;

import java.util.concurrent.atomic.AtomicReference;

import io.github.fabriccommunity.events.play.ItemEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

/**
 * Some longer mixin implementations are done in classes such as this one in order to make debugging easier.
 */
public final class InteractionsImpl {
	private InteractionsImpl() {
	}

	public static ItemStack eatFood(LivingEntity self, World world, ItemStack original) {
		AtomicReference<ItemStack> eaten = new AtomicReference<>(original);
		AtomicReference<ItemStack> moddedResultReference = new AtomicReference<>();
		ActionResult eventResult = ItemEvents.EAT_FOOD.invoker().onEatFood(self, world, original, eaten, moddedResultReference);

		ItemStack defaultResult = eventResult == ActionResult.FAIL ? original : self.eatFood(world, eaten.get());
		ItemStack moddedResult = moddedResultReference.get();
		return moddedResult == null ? defaultResult : moddedResult;
	}
}
