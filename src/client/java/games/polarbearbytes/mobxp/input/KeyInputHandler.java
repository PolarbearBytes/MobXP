package games.polarbearbytes.mobxp.input;

import games.polarbearbytes.mobxp.gui.screens.MobXPListScreen;
import games.polarbearbytes.mobxp.networking.MobXPDataRequestPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * Keybinding class
 */
public class KeyInputHandler {
    public static final KeyBinding.Category KEY_CATEGORY_KEYS = KeyBinding.Category.create(Identifier.of("mobxp","keys"));
    public static final String KEY_TOGGLE_SCREEN = "key.mobxp.openxpscreen";

    public static KeyBinding toggleXPScreenKey;

    /**
     * Register the actual actions for the keybinds
     */
    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(toggleXPScreenKey.wasPressed()){
                if(client.player == null) return;
                MobXPListScreen screen = new MobXPListScreen();
                MinecraftClient.getInstance().setScreen(screen);
                ClientPlayNetworking.send(new MobXPDataRequestPacket());
            }
        });
    }

    /**
     * Register the keybinds
     */
    public static void register() {
        toggleXPScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_TOGGLE_SCREEN,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_5,
                KEY_CATEGORY_KEYS
        ));

        registerKeyInputs();
    }
}

