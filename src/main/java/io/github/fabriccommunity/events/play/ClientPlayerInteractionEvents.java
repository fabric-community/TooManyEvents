package io.github.fabriccommunity.events.play;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.HitResult;

/**
 * A bunch of *client-side* events for player interaction.
 */
@Environment(EnvType.CLIENT)
public final class ClientPlayerInteractionEvents {
    public static final Event<AttackKeyPress> ATTACK_KEY_PRESS = EventFactory.createArrayBacked(AttackKeyPress.class, listeners -> (player, hitResult) -> {
        for (AttackKeyPress listener : listeners) {
            ActionResult result = listener.onAttackKeyPress(player, hitResult);

            if (result != ActionResult.PASS) {
                return result;
            }
        }

        return ActionResult.PASS;
    });

    public static final Event<AttackKeyHold> ATTACK_KEY_HOLD = EventFactory.createArrayBacked(AttackKeyHold.class, listeners -> (player, hitResult) -> {
        for (AttackKeyHold listener : listeners) {
            ActionResult result = listener.onAttackKeyHold(player, hitResult);

            if (result != ActionResult.PASS) {
                return result;
            }
        }

        return ActionResult.PASS;
    });

    public static final Event<TypeLetter> TYPE_LETTER = EventFactory.createArrayBacked(TypeLetter.class, listeners -> (chr, keycode) -> {
        for (TypeLetter listener : listeners) {
            ActionResult result = listener.onType(chr, keycode);

            if (result != ActionResult.PASS) {
                return result;
            }
        }

        return ActionResult.PASS;
    });

    /**
     * An event which runs when the player presses down the attack key (left mouse button by default) in-game.
     * This event does NOT run when the player has a screen of any sort opened, and is called *before* AttackBlockEvent and AttackEntityEvent.
     *
     * @author leocth
     */
    @FunctionalInterface
    public interface AttackKeyPress {
        /**
         * @param player the attacking player.
         * @param hitResult the result of the hit (miss/block/entity), might be null.
         *
         * @return
         * <ul>
         * <li>{@code SUCCESS} or {@code CONSUME} cancels further event processing and swings the player's arms.
         * <li>{@code PASS} pass event handling on to further processing. If all listeners pass, vanilla behavior is executed instead.
         * <li>{@code FAIL} cancels further event processing without swinging the player's arms.
         * </ul>
         */
        ActionResult onAttackKeyPress(ClientPlayerEntity player, /* @Nullable */ HitResult hitResult);
    }

    /**
     * An event which runs when the player holds down the attack key (left mouse button by default) in-game.
     * This event does NOT run when the player has a screen of any sort opened.
     *
     * @author leocth
     */
    @FunctionalInterface
    public interface AttackKeyHold {
        /**
         * @param player the attacking player.
         * @param hitResult the result of the hit (miss/block/entity), might be null.
         *
         * @return
         * <ul>
         * <li>{@code SUCCESS} or {@code CONSUME} cancels further event processing and swings the player's arms.
         * <li>{@code PASS} pass event handling on to further processing. If all listeners pass, vanilla behavior is executed instead.
         * <li>{@code FAIL} cancels further event processing without swinging the player's arms.
         * </ul>
         */
        ActionResult onAttackKeyHold(ClientPlayerEntity player, /* @Nullable */ HitResult hitResult);
    }

    /**
     * An event which runs when the player types a letter in the chat text field.
     * @author leocth
     */
    @FunctionalInterface
    public interface TypeLetter {
        /**
         * @param chr the character being typed
         * @param keyCode the keycode being pressed
         * @see org.lwjgl.glfw.GLFW for more info on key codes.
         *
         * @return
         * {@code SUCCESS} or {@code CONSUME} cancels further event processing and the character is written into the text field.
         * {@code PASS} pass event handling on to further processing. If all listeners pass, functions exactly the same as {@code SUCCESS} and {@code CONSUME}.
         * {@code FAIL} cancels further event processing and disallows the character to be written into the text field.
         */
        ActionResult onType(char chr, int keyCode);
    }
}
