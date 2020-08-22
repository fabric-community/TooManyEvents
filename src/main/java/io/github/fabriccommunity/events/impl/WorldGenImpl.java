package io.github.fabriccommunity.events.impl;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.fabriccommunity.events.world.WorldGenEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public final class WorldGenImpl {
	private WorldGenImpl() {
	}

	/**
	 * @author Valoeghese
	 */
	public static void onBiomePlaced(BiomeSource source, Registry<Biome> registry, int genX, int genZ, CallbackInfoReturnable<Biome> info) {
		Biome original = info.getReturnValue();
		AtomicReference<Biome> biome = new AtomicReference<Biome>();
		ActionResult result = WorldGenEvents.BIOME_PLACEMENT.invoker().onBiomePlace(source, registry, original, biome, genX, genZ);

		if (result != ActionResult.FAIL) {
			info.setReturnValue(biome.get());
		}
	}
}
