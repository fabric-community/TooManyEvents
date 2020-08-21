package io.github.fabriccommunity.events.world;

import java.util.concurrent.atomic.AtomicReference;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

/**
 * Called after biome placement is decided to allow for mods to edit the biome to be placed.
 * @author Valoeghese
 */
@FunctionalInterface
public interface BiomePlacementCallback {
	/**
	 * Event for vanilla layered biome sources. Most commonly the overworld.
	 */
	Event<BiomePlacementCallback> VANILLA_LAYERED = EventFactory.createArrayBacked(BiomePlacementCallback.class, listeners -> (registry, biome, genX, genZ) -> {
		Biome vanilla = biome.get();

		for (BiomePlacementCallback callback : listeners) {
			biome.set(vanilla);

			if (callback.onBiomePlace(registry, biome, genX, genZ)) {
				return true;
			}
		}

		return false;
	});

	/**
	 * Event for multi noise biome sources. Most commonly the nether, but datapacks and some mods may also use this.
	 */
	Event<BiomePlacementCallback> MULTI_NOISE = EventFactory.createArrayBacked(BiomePlacementCallback.class, listeners -> (registry, biome, genX, genZ) -> {
		Biome vanilla = biome.get();

		for (BiomePlacementCallback callback : listeners) {
			biome.set(vanilla);

			if (callback.onBiomePlace(registry, biome, genX, genZ)) {
				return true;
			}
		}

		return false;
	});

	Event<BiomePlacementCallback> THE_END = EventFactory.createArrayBacked(BiomePlacementCallback.class, listeners -> (registry, biome, genX, genZ) -> {
		Biome vanilla = biome.get();

		for (BiomePlacementCallback callback : listeners) {
			biome.set(vanilla);

			if (callback.onBiomePlace(registry, biome, genX, genZ)) {
				return true;
			}
		}

		return false;
	});

	/**
	 * Called on biome placement. Change the biome reference parameter and return true to replace the biome.
	 * @param registry the biome registry.
	 * @param biome the biome that is to generate. Will be equal to the biome that is otherwise to generate at the beginning of the event.
	 * @param genX the generation x of the biome. Equal to {@code x >> 2}.
	 * @param genZ the generation z of the biome. Equal to {@code z >> 2}.
	 * @return true if the biome should be replaced by the biome stored in the {@code biome} parameter, false to revert the biome to the original and continue event processing.
	 */
	boolean onBiomePlace(Registry<Biome> registry, AtomicReference<Biome> biome, int genX, int genZ);
}
