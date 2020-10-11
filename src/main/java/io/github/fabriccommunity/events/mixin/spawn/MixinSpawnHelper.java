package io.github.fabriccommunity.events.mixin.spawn;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.fabriccommunity.events.impl.EntitySpawnImpl;
import io.github.fabriccommunity.events.world.WorldGenEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;

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

	@Inject(at = @At("RETURN"), method = "populateEntities")
	private static void onPopulateEntities(ServerWorldAccess world, Biome biome, int chunkX, int chunkZ, Random random, CallbackInfo info) {
		WorldGenEvents.POPULATE_ENTITIES.invoker().onPopulateEntities(world, biome, chunkX, chunkZ, random, MixinSpawnHelper::getEntitySpawnPos);
	}

	@Shadow
	private static BlockPos getEntitySpawnPos(WorldView world, EntityType<?> entityType, int x, int z) {
		throw new RuntimeException("Mixin getEntitySpawnPos failed to apply!");
	}
}
