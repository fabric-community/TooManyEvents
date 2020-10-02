package io.github.fabriccommunity.events.mixin.advancement;

import io.github.fabriccommunity.events.play.PlayerAdvancementEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.advancement.criterion.ItemDurabilityChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ItemDurabilityChangedCriterion.class)
public class MixinItemDurabilityChangedCriterion {
	@Inject(method = "trigger", at = @At("HEAD"))
	private void injectEvent(ServerPlayerEntity player, ItemStack stack, int damage, CallbackInfo info) {
		PlayerAdvancementEvents.ITEM_DURABILITY_CHANGED_WILDCARD.invoker().onItemDurabilityChange(player, stack, damage);
		PlayerAdvancementEvents.itemDurabilityChanged(stack.getItem()).invoker().onItemDurabilityChange(player, stack, damage);
	}
}
