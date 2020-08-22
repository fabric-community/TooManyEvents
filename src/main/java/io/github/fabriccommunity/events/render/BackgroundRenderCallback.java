package io.github.fabriccommunity.events.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.biome.Biome;

public final class BackgroundRenderCallback {
	/**
	 * Event for altering the biome fog color.
	 */
	public static final Event<BiomeFogColor> BIOME_FOG_COLOR = EventFactory.createArrayBacked(BiomeFogColor.class, listeners -> (dimension, biome, bx, by, bz, color) -> {
		for (BiomeFogColor listener : listeners) {
			color = listener.modifyFogColor(dimension, biome, bx, by, bz, color);
		}

		return color;
	});

	/**
	 * Event for biome fog color.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface BiomeFogColor {
		/**
		 * Alters the fog color.
		 * @param dimension the world in which the player is.
		 * @param biome the biome providing the fog color.
		 * @param biomeX the x position of the biome on the biome grid. Equal to blockX >> 2.
		 * @param biomeY the y position of the biome on the biome grid. Equal to blockY >> 3.
		 * @param biomeZ the z position of the biome on the biome grid. Equal to blockZ >> 2.
		 * @param currentColor the current fog color.
		 * @return the new fog color to use.
		 */
		int modifyFogColor(ClientWorld world, Biome biome, int biomeX, int biomeY, int biomeZ, int currentColor);
	}
}
