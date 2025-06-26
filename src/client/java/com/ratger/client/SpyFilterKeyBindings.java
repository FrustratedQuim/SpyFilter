package com.ratger.client;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SpyFilterKeyBindings {
    private static final KeyBinding SPY_CHAT_TOGGLE_KEY = new KeyBinding(
            "key.spyfilter.toggle_spy_chat",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.spyfilter"
    );

    private static boolean isSpyChatVisible = true;

    public static KeyBinding getSpyChatToggleKey() {
        return SPY_CHAT_TOGGLE_KEY;
    }

    public static void toggleSpyChat() {
        isSpyChatVisible = !isSpyChatVisible;
    }

    public static boolean isSpyChatVisible() {
        return isSpyChatVisible;
    }
}
