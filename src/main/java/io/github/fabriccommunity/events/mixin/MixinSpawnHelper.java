package io.github.fabriccommunity.events.mixin;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.EntitySpawnCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.SpawnHelper;

/**
 * @author Valoeghese
 */
@Mixin(SpawnHelper.class)
public class MixinSpawnHelper {
	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"),
			method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V"
			)
	private static void entitySpawnEventNatural(ServerWorld self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self, SpawnReason.NATURAL);
		entity = currentEntity.get();

		if (result == ActionResult.SUCCESS) {
			self.spawnEntityAndPassengers(entity);
			EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), SpawnReason.NATURAL);
		}
	}

	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"),
			method = "populateEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/biome/Biome;IILjava/util/Random;)V"
			)
	private static void entitySpawnEventChunk(ServerWorldAccess self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self, SpawnReason.CHUNK_GENERATION);
		entity = currentEntity.get();

		if (result == ActionResult.SUCCESS) {
			self.spawnEntityAndPassengers(entity);
			EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), SpawnReason.CHUNK_GENERATION);
		}
	}
}
