package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.world.WorldGenEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class ChunkGenTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			WorldGenEvents.GENERATE_FEATURES_START.register((generator, region, structures) -> {
				if (random(region.getCenterChunkX(), region.getCenterChunkZ(), (int) region.getSeed(), 0b111) == 0) {
					return true; // cancel further event processing
				}

				return false;
			});

			WorldGenEvents.GENERATE_FEATURES_END.register((generator, region, structures) -> {
				int x = region.getCenterChunkX() * 16;
				int z = region.getCenterChunkZ() * 16;
				BlockPos.Mutable e = new BlockPos.Mutable(x, 0, z);

				for (int y = 128; y >= 32; --y) {
					e.setY(y);
					region.setBlockState(e, Blocks.AIR.getDefaultState(), 3);
				}
			});

			WorldGenEvents.POPULATE_ENTITIES.register((world, biome, chunkX, chunkZ, random, spawnPosGetter) -> {
				if (biome.getCategory() != Biome.Category.OCEAN) {
					int target = random.nextInt(3);

					for (int i = 0; i < target; ++i) {
						int ex = (chunkX << 4) + random.nextInt(16);
						int ez = (chunkZ << 4) + random.nextInt(16);

						VillagerEntity villager = EntityType.VILLAGER.create(world.toServerWorld());
						BlockPos pos = spawnPosGetter.getEntitySpawnPos(world, villager.getType(), ex, ez);
						villager.refreshPositionAndAngles(pos, random.nextFloat() * 360.0f, 0.0f);
						villager.headYaw = villager.yaw;
						villager.bodyYaw = villager.yaw;
						villager.initialize(world, world.getLocalDifficulty(pos), SpawnReason.STRUCTURE, null, null);
						world.spawnEntityAndPassengers(villager);
					}
				}
			});
		}
	}

	private static int random(int x, int y, int seed, int mask) {
		seed = 375462423 * seed + 672456235;
		seed += x;
		seed = 375462423 * seed + 672456235;
		seed += y;
		seed = 375462423 * seed + 672456235;
		return seed & mask;
	}
}
