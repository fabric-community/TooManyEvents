package io.github.fabriccommunity.events.mixin.world;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.fabriccommunity.events.world.WorldGenEvents;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

/**
 * @author Valoeghese
 */
@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator {
	@Inject(at = @At("HEAD"), method = "generateFeatures", cancellable = true)
	private void onStartGenerateFeatures(ChunkRegion region, StructureAccessor accessor, CallbackInfo info) {
		if (WorldGenEvents.GENERATE_FEATURES_START.invoker().onStartGenerateFeatures((ChunkGenerator) (Object) this, region, accessor)) {
			info.cancel();
		}
	}

	@Inject(at = @At("RETURN"), method = "generateFeatures")
	private void onEndGenerateFeatures(ChunkRegion region, StructureAccessor accessor, CallbackInfo info) {
		WorldGenEvents.GENERATE_FEATURES_END.invoker().onEndGenerateFeatures((ChunkGenerator) (Object) this, region, accessor);
	}
}
