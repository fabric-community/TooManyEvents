package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.EntitySpawnCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.ActionResult;

public class EntitySpawnTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			EntitySpawnCallback.PRE.register((original, entity, world, reason) -> {
				if (reason == SpawnReason.SPAWN_EGG && original.getType() == EntityType.CREEPER) {
					entity.set(new PigEntity(EntityType.PIG, world.getWorld()));
				}

				return ActionResult.PASS;
			});

			EntitySpawnCallback.POST.register((entity, world, pos, reason) -> {
				if (entity.getType() == EntityType.CREEPER) {
					((CreeperEntity) entity).onStruckByLightning(new LightningEntity(EntityType.LIGHTNING_BOLT, world.getWorld()));
				}
			});
		}
	}
}
