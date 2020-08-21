package io.github.fabriccommunity.events.impl;

import java.util.concurrent.atomic.AtomicReference;

import io.github.fabriccommunity.events.EntitySpawnCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.world.ServerWorldAccess;

public class EntitySpawnImpl {
	public static boolean spawnEntityZ(ServerWorld self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self, SpawnReason.COMMAND);
		entity = currentEntity.get();
		System.out.println(entity);

		if (result == ActionResult.SUCCESS) {
			if (self.method_30736(entity)) {
				EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), SpawnReason.COMMAND);
				return true;
			}
		}

		return false;
	}

	public static void spawnEntityV(ServerWorldAccess self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self, SpawnReason.NATURAL);
		entity = currentEntity.get();

		if (result == ActionResult.SUCCESS) {
			self.spawnEntityAndPassengers(entity);
			EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), SpawnReason.NATURAL);
		}
	}

}
