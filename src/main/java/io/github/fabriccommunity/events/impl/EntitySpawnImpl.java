package io.github.fabriccommunity.events.impl;

import java.util.concurrent.atomic.AtomicReference;

import io.github.fabriccommunity.events.EntitySpawnCallback;
import io.github.fabriccommunity.events.EntitySpawnCallback.Pre;
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

		if (result != ActionResult.FAIL) {
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

		if (result != ActionResult.FAIL) {
			self.spawnEntityAndPassengers(entity);
			EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), SpawnReason.NATURAL);
		}
	}

	public static ActionResult eventPre(Entity original, AtomicReference<Entity> entity, ServerWorldAccess world, SpawnReason reason, Pre[] listeners) {
		for (EntitySpawnCallback.Pre callback : listeners) {
			ActionResult result = callback.onEntitySpawnPre(original, entity, world, reason);

			if (result != ActionResult.PASS) {
				return result;
			}
		}

		return ActionResult.PASS;
	}
}
