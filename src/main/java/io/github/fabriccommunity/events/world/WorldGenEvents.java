package io.github.fabriccommunity.events.world;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import io.github.fabriccommunity.events.util.SpawnPosGetter;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

/**
 * A collection of events pertaining to world generation.
 */
public final class WorldGenEvents {
	/**
	 * Called at the end of biome placement to allow for mods to edit the biome to be placed.
	 */
	public static final Event<BiomePlacement> BIOME_PLACEMENT = EventFactory.createArrayBacked(BiomePlacement.class, listeners -> (source, registry, original, biome, genX, genZ) -> {
		for (BiomePlacement listener : listeners) {
			ActionResult result = listener.onBiomePlace(source, registry, original, biome, genX, genZ);

			if (result != ActionResult.PASS) {
				return result;
			}
		}

		return ActionResult.PASS;
	});

	/**
	 * Called on feature placement to allow for mods to modify the chunk without using mojang's "features", or even replace chunk feature population, among various other things.
	 */
	public static final Event<GenerateFeaturesStart> GENERATE_FEATURES_START = EventFactory.createArrayBacked(GenerateFeaturesStart.class, listeners -> (generator, region, structures) -> {
		for (GenerateFeaturesStart listener : listeners) {
			if (listener.onStartGenerateFeatures(generator, region, structures)) {
				return true;
			}
		}

		return false;
	});

	/**
	 * Called at the end of biome placement to allow for mods to edit the biome to be placed.
	 */
	public static final Event<GenerateFeaturesEnd> GENERATE_FEATURES_END = EventFactory.createArrayBacked(GenerateFeaturesEnd.class, listeners -> (generator, region, structures) -> {
		for (GenerateFeaturesEnd listener : listeners) {
			listener.onEndGenerateFeatures(generator, region, structures);
		}
	});

	/**
	 * Called on chunk generation entity population to allow for mods to spawn entities with their own logic.
	 */
	public static final Event<PopulateEntities> POPULATE_ENTITIES = EventFactory.createArrayBacked(PopulateEntities.class, listeners -> (world, biome, chunkX, chunkZ, random, spawnPosGetter) -> {
		for (PopulateEntities listener : listeners) {
			listener.onPopulateEntities(world, biome, chunkX, chunkZ, random, spawnPosGetter);
		}
	});

	/**
	 * Called on feature placement to allow for mods to modify the chunk without using mojang's "features", among various other things.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface BiomePlacement {
		/**
		 * Called on biome placement.
		 * @param BiomeSource the biome source.
		 * @param registry the biome registry.
		 * @param original the original biome to generate.
		 * @param biome the biome that is to generate. If the biome stored in this is different to {@code original}, the biome has been replaced by another listener.
		 * @param genX the generation x of the biome. Equal to {@code x >> 2}.
		 * @param genZ the generation z of the biome. Equal to {@code z >> 2}.
		 * @return
		 * <ul>
		 * <li>{@code SUCCESS} or {@code CONSUME} to cancel further event processing and return the biome stored in the {@code biome} parameter.
		 * <li>{@code PASS} to pass the event on to further event processing. If all listeners pass, it is treated as a {@code SUCCESS}
		 * <li>{@code FAIL} to cancel further event processing and return the original biome.
		 * </ul>
		 */
		ActionResult onBiomePlace(BiomeSource source, Registry<Biome> registry, final Biome original, AtomicReference<Biome> biome, int genX, int genZ);
	}

	/**
	 * Called on feature placement to allow for mods to modify the chunk without using mojang's "features", or even replace chunk feature population, among various other things.
	 * @return true to cancel further event processing and not place features through vanilla's method. false to pass on to further event processing.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface GenerateFeaturesStart {
		boolean onStartGenerateFeatures(ChunkGenerator generator, ChunkRegion region, StructureAccessor accessor);
	}

	/**
	 * Called on feature placement to allow for mods to modify the chunk without using mojang's "features", among various other things.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface GenerateFeaturesEnd {
		void onEndGenerateFeatures(ChunkGenerator generator, ChunkRegion region, StructureAccessor accessor);
	}

	/**
	 * Called on chunk generation entity population to allow for mods to spawn entities with their own logic.
	 * @param spawnPosGetter a function used by vanilla to get the spawn block pos for the given x/z coordinates.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface PopulateEntities {
		void onPopulateEntities(ServerWorldAccess world, Biome biome, int chunkX, int chunkZ, Random random, SpawnPosGetter spawnPosGetter);
	}
}
