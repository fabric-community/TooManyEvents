package io.github.fabriccommunity.events.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.render.BackgroundRenderCallback;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getFogColor()I"),
			method = "method_24873(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/world/biome/source/BiomeAccess;FIII)Lnet/minecraft/util/math/Vec3d;"
			)
	private static int modifyFogColour(Biome self, ClientWorld world, BiomeAccess biomeAccess, float sunHeight, int genX, int genY, int genZ) {
		return BackgroundRenderCallback.BIOME_FOG_COLOR.invoker().modifyFogColor(world, self, genX, genY, genZ, self.getFogColor());
	}
}
