package io.github.fabriccommunity.events.mixin.biomegen;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.fabriccommunity.events.world.BiomePlacementCallback;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

/**
 * @author Valoeghese
 */
@Mixin(MultiNoiseBiomeSource.class)
public class MixinMultiNoiseBiomeSource {
	@Inject(at = @At("RETURN"), method = "getBiomeForNoiseGen", cancellable = true)
	private void injectBiomePlacementEventOverworld(int genX, int useless, int genZ, CallbackInfoReturnable<Biome> arr) {
		AtomicReference<Biome> funni = new AtomicReference<Biome>(arr.getReturnValue());
		// TODO is there a per-world registry in this biome source?
		boolean replace = BiomePlacementCallback.MULTI_NOISE.invoker().onBiomePlace(BuiltinRegistries.BIOME, funni, genX, genZ);

		if (replace) {
			arr.setReturnValue(funni.get());
		}
	}
}
