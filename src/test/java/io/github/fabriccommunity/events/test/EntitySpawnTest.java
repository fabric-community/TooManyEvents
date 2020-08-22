package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.world.EntitySpawnCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

/**
 * @author Valoeghese
 */
public class EntitySpawnTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			EntitySpawnCallback.PRE.register((original, entity, world, reason) -> {
				//if (reason == SpawnReason.COMMAND) return ActionResult.SUCCESS;
				if (reason == SpawnReason.SPAWN_EGG && original.getType() == EntityType.CREEPER) {
					PigEntity pig = EntityType.PIG.create(world.toServerWorld());
					Vec3d pos = original.getPos();
					pig.refreshPositionAndAngles(pos.x, pos.y, pos.z, original.yaw, original.pitch);
					pig.headYaw = pig.yaw;
					pig.bodyYaw = pig.yaw;
					pig.initialize(world, world.getLocalDifficulty(original.getBlockPos()), reason, null, null);
					entity.set(pig);
					return ActionResult.SUCCESS;
				}

				return ActionResult.PASS;
			});

			EntitySpawnCallback.POST.register((entity, world, pos, reason) -> {
				if (entity.getType() == EntityType.CREEPER && reason == SpawnReason.COMMAND) { // use a command /summon to test this
					LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world.toServerWorld());
					lightning.refreshPositionAndAngles(entity.prevX + 1, entity.prevY, entity.prevZ - 1, entity.yaw, entity.pitch);
					world.spawnEntity(lightning);
				}
			});
		}
	}
}
