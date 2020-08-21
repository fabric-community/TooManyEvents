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
import net.minecraft.world.MobSpawnerLogic;

/**
 * @author Valoeghese
 */
@Mixin(MobSpawnerLogic.class)
public class MixinMobSpawnerLogic {
	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;method_30736(Lnet/minecraft/entity/Entity;)Z"),
			method = "update()V"
			)
	private boolean entitySpawnEventSpawner(ServerWorld self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self, SpawnReason.SPAWNER);
		entity = currentEntity.get();

		if (result == ActionResult.SUCCESS) {
			if (self.method_30736(entity)) {
				EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), SpawnReason.SPAWNER);
				return true;
			}
		}

		return false;
	}
}
