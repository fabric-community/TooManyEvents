package io.github.fabriccommunity.events.mixin.player;

import java.util.concurrent.atomic.AtomicInteger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.fabriccommunity.events.play.PlayerInteractionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

/**
 * @author HalfOf2, Valoeghese
 */
@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
	@ModifyVariable(at = @At(value = "HEAD"),
			method = "addExperience",
			index = 1)
	private int modifyExperience(int original) {
		AtomicInteger experience = new AtomicInteger(original);
		ActionResult result = PlayerInteractionEvents.XP_GAIN.invoker().onXPGained((PlayerEntity) (Object) this, original, experience);

		if (result == ActionResult.FAIL) {
			return original;
		} else {
			return experience.get();
		}
	}

	@Inject(at = @At("HEAD"), method = "wakeUp(ZZ)V")
	private void wakeUp(boolean bl, boolean updateSleepingPlayers, CallbackInfo info) {
		PlayerInteractionEvents.WAKE_UP.invoker().onWakeUp((PlayerEntity) (Object) this, updateSleepingPlayers, bl);
	}
}
