package io.github.fabriccommunity.events.world;

import java.util.concurrent.atomic.AtomicReference;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

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
	 * Called at the end of biome placement to allow for mods to edit the biome to be placed.
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
		 * {@code SUCCESS} or {@code CONSUME} to cancel further event processing and return the biome stored in the {@code biome} parameter.
		 * {@code PASS} to pass the event on to further event processing. If all listeners pass, it is treated as a {@code SUCCESS}
		 * {@code FAIL} to cancel further event processing and return the original biome.
		 */
		ActionResult onBiomePlace(BiomeSource source, Registry<Biome> registry, final Biome original, AtomicReference<Biome> biome, int genX, int genZ);
	}
}
