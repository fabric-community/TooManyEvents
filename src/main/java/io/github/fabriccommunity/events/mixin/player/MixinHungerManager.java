package io.github.fabriccommunity.events.mixin.player;

import java.util.concurrent.atomic.AtomicInteger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.play.PlayerInteractionEvents;
import io.github.fabriccommunity.events.util.WrappedFloat;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

@Mixin(HungerManager.class)
public class MixinHungerManager {
	@ModifyVariable(at = @At(value = "HEAD"),
			method = "add",
			index = 1)
	private int modifyFood(int original) {
		AtomicInteger food = new AtomicInteger(original);
		ActionResult result = PlayerInteractionEvents.RESTORE_HUNGER.invoker().onRestoreHunger((HungerManager) (Object) this, original, food);

		if (result == ActionResult.FAIL) {
			return original;
		} else {
			return food.get();
		}
	}

	@ModifyVariable(at = @At(value = "HEAD"),
			method = "add",
			index = 2)
	private float modifySaturation(float original) {
		WrappedFloat saturation = new WrappedFloat(original);
		ActionResult result = PlayerInteractionEvents.RESTORE_SATURATION.invoker().onRestoreSaturation((HungerManager) (Object) this, original, saturation);

		if (result == ActionResult.FAIL) {
			return original;
		} else {
			return saturation.get();
		}
	}

	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"),
			method = "eat")
	private void consumeStackEvent(HungerManager karenWantsMe, int originalHunger, float originalSaturation, Item itemParam, ItemStack stackParam) {
		AtomicInteger hunger = new AtomicInteger(originalHunger);
		WrappedFloat saturation = new WrappedFloat(originalSaturation);
		ActionResult result = PlayerInteractionEvents.CONSUME_STACK.invoker().onRestoreSaturation(karenWantsMe, stackParam, originalHunger, originalSaturation, hunger, saturation);

		if (result != ActionResult.FAIL) {
			karenWantsMe.add(hunger.get(), saturation.get());
		}
	}
}
