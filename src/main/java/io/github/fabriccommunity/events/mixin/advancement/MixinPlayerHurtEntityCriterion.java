package io.github.fabriccommunity.events.mixin.advancement;

import io.github.fabriccommunity.events.play.PlayerAdvancementEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.advancement.criterion.PlayerHurtEntityCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author b0undary
 */
@Mixin(PlayerHurtEntityCriterion.class)
public class MixinPlayerHurtEntityCriterion {
	@Inject(method = "trigger", at = @At("HEAD"))
	private void injectEvent(ServerPlayerEntity player, Entity entity, DamageSource source, float dealt, float taken, boolean blocked, CallbackInfo info) {
		PlayerAdvancementEvents.PLAYER_HURT_ENTITY_WILDCARD.invoker().onPlayerHurtEntity(player, entity, source, dealt, taken, blocked);
		PlayerAdvancementEvents.playerHurtEntity(entity.getType()).invoker().onPlayerHurtEntity(player, entity, source, dealt, taken, blocked);
	}
}
