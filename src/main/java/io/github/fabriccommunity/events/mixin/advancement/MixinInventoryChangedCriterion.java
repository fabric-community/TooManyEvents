package io.github.fabriccommunity.events.mixin.advancement;

import io.github.fabriccommunity.events.play.PlayerAdvancementEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(InventoryChangedCriterion.class)
public class MixinInventoryChangedCriterion {
	@Inject(method = "trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/item/ItemStack;III)V", at = @At("HEAD"))
	private void injectEvent(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack, int full, int empty, int occupied, CallbackInfo info) {
		PlayerAdvancementEvents.INVENTORY_CHANGED.invoker().onInventoryChange(player, inventory, stack, full, empty, occupied);
	}
}
