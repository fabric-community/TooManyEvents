package io.github.fabriccommunity.events.mixin;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.EntitySpawnCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Valoeghese
 */
@Mixin(EntityType.class)
public class MixinEntityType {
	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
			method = "spawn(Lnet/minecraft/world/World;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"
			)
	private boolean entitySpawnEventSpawner(World self, Entity entity,
			World worldParam, CompoundTag itemTagParam, Text nameParam, PlayerEntity playerParam, BlockPos posParam, SpawnReason spawnReasonParam, boolean alignPositionParam, boolean invertYParam) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self, spawnReasonParam);
		entity = currentEntity.get();

		if (result == ActionResult.SUCCESS) {
			if (self.spawnEntity(entity)) {
				EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), spawnReasonParam);
				return true;
			}
		}

		return false;
	}
}
