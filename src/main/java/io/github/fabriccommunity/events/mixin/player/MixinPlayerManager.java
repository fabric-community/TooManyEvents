package io.github.fabriccommunity.events.mixin.player;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import io.github.fabriccommunity.events.network.PlayerLoginCallback;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;

/**
 * @author HalfOf2, Valoeghese
 */
@Mixin(PlayerManager.class)
public class MixinPlayerManager {
	@Inject(method = "checkCanJoin", at = @At("HEAD"), cancellable = true)
	private void canJoin(SocketAddress socketAddress, GameProfile gameProfile, CallbackInfoReturnable<Text> cir) {
		AtomicReference<Text> denyText = new AtomicReference<>(null); // use atomic for built in set/get stuff
		PlayerLoginCallback.EVENT.invoker().onPlayerLogin(denyText, socketAddress, gameProfile); // request events
		Text deny = denyText.get();

		if(deny != null) { // if the mods wish to deny a player, deny them.
			cir.setReturnValue(deny);
		}
	}
}
