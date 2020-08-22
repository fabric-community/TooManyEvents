package io.github.fabriccommunity.events.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.impl.InteractionsImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(Item.class)
public class MixinItem {
	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"),
			method = "finishUsing"
			)
	private ItemStack onEatFood(LivingEntity self, World world, ItemStack original) {
		return InteractionsImpl.eatFood(self, world, original);
	}
}
