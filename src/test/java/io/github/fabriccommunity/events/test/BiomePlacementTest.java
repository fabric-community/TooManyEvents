package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.world.BiomePlacementCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.biome.BiomeKeys;

public class BiomePlacementTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			BiomePlacementCallback.VANILLA_LAYERED.register((registry, biome, genX, genZ) -> {
				genX = genX >> 2;
				genZ = genZ >> 2;

				// Lazy Man (or woman)'s RNG
				if (((((genX * 6547 >> 2) + genZ * 7) ^ (genX + genZ)) & 0b111) == 0) {
					biome.set(registry.get(BiomeKeys.BADLANDS_PLATEAU));
					return true;
				}

				return false;
			});
		}
	}
}
