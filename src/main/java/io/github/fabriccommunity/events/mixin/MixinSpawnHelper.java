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
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldAccess;

@Mixin(SpawnHelper.class)
public class MixinSpawnHelper {
	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
			method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V"
			)
	private static boolean entitySpawnEventNatural(ServerWorld self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self, SpawnReason.NATURAL);
		entity = currentEntity.get();

		if (result == ActionResult.SUCCESS) {
			if (self.spawnEntity(entity)) {
				EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), SpawnReason.NATURAL);
				return true;
			}
		}

		return false;
	}

	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
			method = "populateEntities(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/world/biome/Biome;IILjava/util/Random;)V"
			)
	private static boolean entitySpawnEventChunk(WorldAccess self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self, SpawnReason.CHUNK_GENERATION);
		entity = currentEntity.get();

		if (result == ActionResult.SUCCESS) {
			if (self.spawnEntity(entity)) {
				EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), SpawnReason.CHUNK_GENERATION);
				return true;
			}
		}

		return false;
	}
}
