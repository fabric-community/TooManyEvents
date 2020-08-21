package io.github.fabriccommunity.events.mixin.player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.player.PlayerEntity;

/**
 * @author HalfOf2, Valoeghese
 */
@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
	// TODO implement
	/*@ModifyVariable(at = @At(value = "HEAD"),
			method = "addExperience",
			index = 0)
	private int modifyExperience(int experience) {
		
	}*/
}
