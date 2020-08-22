package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.world.WorldGenEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.ActionResult;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;

/**
 * @author Valoeghese
 */
public class BiomePlacementTest implements ModInitializer {
	public static final boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			WorldGenEvents.BIOME_PLACEMENT.register((source, registry, original, biome, genX, genZ) -> {
				if (source instanceof VanillaLayeredBiomeSource) {
					genX = genX >> 2;
					genZ = genZ >> 2;

					// Lazy Man (or woman)'s RNG
					if (((((genX * 6547 >> 2) + genZ * 7) ^ (genX + genZ)) & 0b111) == 0) {
						biome.set(registry.get(BiomeKeys.BADLANDS_PLATEAU));
						return ActionResult.SUCCESS;
					}
				}

				return ActionResult.PASS;
			});
		}
	}
}
