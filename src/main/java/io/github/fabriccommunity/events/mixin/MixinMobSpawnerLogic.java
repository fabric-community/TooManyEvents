package io.github.fabriccommunity.events.mixin;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.EntitySpawnCallback;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;

@Mixin(MobSpawnerLogic.class)
public class MixinMobSpawnerLogic {
	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
			method = "spawnEntity(Lnet/minecraft/entity/Entity;)V"
			)
	private boolean epic_entitySpawnEventSpawner(World self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		ActionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self, true);
		entity = currentEntity.get();

		if (result == ActionResult.SUCCESS) {
			if (self.spawnEntity(entity)) {
				EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.getPos(), true);
				return true;
			}
		}

		return false;
	}
}
