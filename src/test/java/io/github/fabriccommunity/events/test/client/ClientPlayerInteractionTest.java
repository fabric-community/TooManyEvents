package io.github.fabriccommunity.events.test.client;

import io.github.fabriccommunity.events.play.ClientPlayerInteractionEvents;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;

/**
 * @author leocth
 */
public class ClientPlayerInteractionTest implements ClientModInitializer {
    public static final boolean ENABLED = true;

    @Override
    public void onInitializeClient() {
        if (ENABLED) {
            ClientPlayerInteractionEvents.ATTACK_KEY_PRESS.register((player, hitResult) -> {
                player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                return ActionResult.SUCCESS;
            });
            ClientPlayerInteractionEvents.ATTACK_KEY_HOLD.register((player, hitResult) -> {
                player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
                return ActionResult.FAIL;
            });
            // message redirector :yeefuckinhaw:
            ClientPlayerInteractionEvents.TYPE_LETTER.register(((chr, keyCode) -> {
                System.out.print(chr);
                return ActionResult.FAIL;
            }
            ));
        }
    }
}
