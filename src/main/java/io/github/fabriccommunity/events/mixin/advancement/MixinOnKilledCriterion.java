package io.github.fabriccommunity.events.mixin.advancement;

import io.github.fabriccommunity.events.play.PlayerAdvancementEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(OnKilledCriterion.class)
public class MixinOnKilledCriterion {
	@Inject(method = "trigger", at = @At("HEAD"))
	private void injectEvent(ServerPlayerEntity player, Entity entity, DamageSource source, CallbackInfo info) {
		if ((Object)this == Criteria.ENTITY_KILLED_PLAYER) {
			PlayerAdvancementEvents.ENTITY_KILLED_PLAYER.invoker().onEntityKillPlayer(player, entity, source);
		} else if ((Object)this == Criteria.PLAYER_KILLED_ENTITY) {
			PlayerAdvancementEvents.PLAYER_KILLED_ENTITY_WILDCARD.invoker().onPlayerKillEntity(player, entity, source);
			PlayerAdvancementEvents.playerKilledEntity(entity.getType()).invoker().onPlayerKillEntity(player, entity, source);
		}
	}
}
