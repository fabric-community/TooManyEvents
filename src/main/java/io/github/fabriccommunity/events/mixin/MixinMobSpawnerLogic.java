package io.github.fabriccommunity.events.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.impl.EntitySpawnImpl;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
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
		return EntitySpawnImpl.spawnEntityZ(self, entity);
	}
}
