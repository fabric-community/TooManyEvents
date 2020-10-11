package io.github.fabriccommunity.events.util;

import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * Used by {@link WorldGenEvents.POPULATE_ENTITIES} as an accessible way to get the entity spawn pos for given x/z coordinates.
 * @author Valoeghese
 */
@FunctionalInterface
public interface SpawnPosGetter {
	BlockPos getEntitySpawnPos(WorldView world, EntityType<?> entityType, int x, int z);
}
