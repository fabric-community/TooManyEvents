package io.github.fabriccommunity.events.mixin.spawn;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.impl.EntitySpawnImpl;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
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
		EntitySpawnImpl.spawnEntityV(self, entity);
	}

	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"),
			method = "populateEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/biome/Biome;IILjava/util/Random;)V"
			)
	private static void entitySpawnEventChunk(ServerWorldAccess self, Entity entity) {
		EntitySpawnImpl.spawnEntityV(self, entity);
	}
}
