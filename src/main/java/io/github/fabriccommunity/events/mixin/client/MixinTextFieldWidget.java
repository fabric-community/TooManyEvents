package io.github.fabriccommunity.events.mixin.client;

import io.github.fabriccommunity.events.play.ClientPlayerInteractionEvents;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author leocth
 */
@Mixin(TextFieldWidget.class)
public class MixinTextFieldWidget {
    @Inject(at = @At("HEAD"), method = "charTyped", cancellable = true)
    public void charTyped(char chr, int keyCode, CallbackInfoReturnable<Boolean> cir) {
        ActionResult result = ClientPlayerInteractionEvents.TYPE_LETTER.invoker().onType(chr, keyCode);
        if (result == ActionResult.FAIL) {
            cir.setReturnValue(false);
        }
    }
}
