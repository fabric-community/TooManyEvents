package io.github.fabriccommunity.events.mixin.biomegen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.fabriccommunity.events.impl.WorldGenImpl;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

/**
 * @author Valoeghese
 */
@Mixin(MultiNoiseBiomeSource.class)
public class MixinMultiNoiseBiomeSource {
	@Inject(at = @At("RETURN"), method = "getBiomeForNoiseGen", cancellable = true)
	private void injectBiomePlacementEventMultiNoise(int genX, int useless, int genZ, CallbackInfoReturnable<Biome> info) {
		// TODO is there a per-world registry in this biome source?
		WorldGenImpl.onBiomePlaced((BiomeSource) (Object) this, BuiltinRegistries.BIOME, genX, genZ, info);
	}
}
