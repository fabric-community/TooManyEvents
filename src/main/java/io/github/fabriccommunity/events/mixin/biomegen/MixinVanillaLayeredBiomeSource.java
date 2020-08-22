package io.github.fabriccommunity.events.mixin.biomegen;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.fabriccommunity.events.impl.WorldGenImpl;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;

/**
 * @author Valoeghese
 */
@Mixin(VanillaLayeredBiomeSource.class)
public class MixinVanillaLayeredBiomeSource {
	@Shadow
	@Final
	private Registry<Biome> biomeRegistry;

	@Inject(at = @At("RETURN"), method = "getBiomeForNoiseGen", cancellable = true)
	private void injectBiomePlacementEventOverworld(int genX, int useless, int genZ, CallbackInfoReturnable<Biome> info) {
		WorldGenImpl.onBiomePlaced((BiomeSource) (Object) this, this.biomeRegistry, genX, genZ, info);
	}
}
