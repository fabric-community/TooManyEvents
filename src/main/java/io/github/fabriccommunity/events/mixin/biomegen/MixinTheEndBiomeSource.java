package io.github.fabriccommunity.events.mixin.biomegen;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.fabriccommunity.events.world.BiomePlacementCallback;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.TheEndBiomeSource;

/**
 * @author Valoeghese
 */
@Mixin(TheEndBiomeSource.class)
public class MixinTheEndBiomeSource {
	@Shadow
	@Final
	private Registry<Biome> biomeRegistry;

	@Inject(at = @At("RETURN"), method = "getBiomeForNoiseGen", cancellable = true)
	private void injectBiomePlacementEventOverworld(int genX, int useless, int genZ, CallbackInfoReturnable<Biome> arr) {
		AtomicReference<Biome> funni = new AtomicReference<Biome>(arr.getReturnValue());
		boolean replace = BiomePlacementCallback.THE_END.invoker().onBiomePlace(this.biomeRegistry, funni, genX, genZ);

		if (replace) {
			arr.setReturnValue(funni.get());
		}
	}
}
