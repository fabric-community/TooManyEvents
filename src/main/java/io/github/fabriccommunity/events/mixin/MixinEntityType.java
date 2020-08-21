package io.github.fabriccommunity.events.mixin;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import io.github.fabriccommunity.events.EntitySpawnCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

/**
 * @author Valoeghese
 */
@Mixin(EntityType.class)
public class MixinEntityType {
	@Overwrite
	public Entity spawn(ServerWorld serverWorld, CompoundTag itemTag, Text name, PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
		Entity entity = ((EntityType) (Object) this).create(serverWorld, itemTag, name, player, pos, spawnReason, alignPosition, invertY);

		if (entity != null) {
			AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
			ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, serverWorld, spawnReason);
			entity = result == ActionResult.SUCCESS ? currentEntity.get() : null;
		}

		if (entity != null) {
			serverWorld.spawnEntityAndPassengers(entity);
			EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, serverWorld, entity.getPos(), spawnReason);
		}

		return entity;
	}
}
