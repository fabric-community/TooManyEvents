package io.github.fabriccommunity.events.mixin.item;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.play.ItemEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

@Mixin(BlockItem.class)
public abstract class MixinBlockItem {
	@Shadow
	protected abstract BlockState getPlacementState(ItemPlacementContext context);

	/**
	 * @reason getPlacementState is often overriden (has two vanilla overrides, could have more in modded). Therefore using a redirect rather than inject at return in {@code getPlacementState} is more compatible with other mods.
	 */
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;"),
			method = "getPlacementState")
	private BlockState onGetPlacementState(BlockItem self, ItemPlacementContext placementContext) {
		BlockState original = this.getPlacementState(placementContext);
		AtomicReference<BlockState> state = new AtomicReference<>(original);

		ActionResult result = ItemEvents.PLACED.invoker().onBlockPlaced(placementContext, original, state);
		BlockState modified = state.get();

		if (result == ActionResult.FAIL) {
			PlayerEntity entity = placementContext.getPlayer();

			if (entity instanceof ServerPlayerEntity) {
				int selectedSlot = entity.inventory.selectedSlot;
				((ServerPlayerEntity) entity).networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(-2, selectedSlot, entity.inventory.getStack(selectedSlot)));
			}

			return null;
		} else if (result == ActionResult.SUCCESS || (original != modified)) {
			return modified;
		} else {
			return original;
		}
	}
}
