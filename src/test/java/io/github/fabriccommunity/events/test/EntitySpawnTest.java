package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.EntitySpawnCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public class EntitySpawnTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			EntitySpawnCallback.PRE.register((original, entity, world, reason) -> {
				if (reason == SpawnReason.SPAWN_EGG && original.getType() == EntityType.CREEPER) {
					PigEntity pig = new PigEntity(EntityType.PIG, world.getWorld());
					Vec3d pos = original.getPos();
					pig.refreshPositionAndAngles(pos.x, pos.y, pos.z, original.yaw, original.pitch);
					pig.initialize(world, world.getLocalDifficulty(original.getBlockPos()), reason, null, null);
					entity.set(pig);
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
