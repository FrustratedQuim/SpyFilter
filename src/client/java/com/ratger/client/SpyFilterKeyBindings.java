package com.ratger.client;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpyFilterKeyBindings {
    private static final Logger LOGGER = LoggerFactory.getLogger("spyfilter");

    private static final KeyBinding SPY_CHAT_TOGGLE_KEY = new KeyBinding(
            "key.spyfilter.toggle_spy_chat",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.spyfilter"
    );
    private static final KeyBinding BASIC_SPY_TOGGLE_KEY = new KeyBinding(
            "key.spyfilter.toggle_basic_spy",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            "category.spyfilter"
    );
    private static final KeyBinding SPY_BOOK_TOGGLE_KEY = new KeyBinding(
            "key.spyfilter.toggle_spy_book",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            "category.spyfilter"
    );
    private static final KeyBinding SPY_SIGN_TOGGLE_KEY = new KeyBinding(
            "key.spyfilter.toggle_spy_sign",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            "category.spyfilter"
    );

    private static boolean isChatSpyVisible = true;
    private static boolean isBookSpyVisible = true;
    private static boolean isSignSpyVisible = true;

    public static KeyBinding getSpyChatToggleKey() {
        return SPY_CHAT_TOGGLE_KEY;
    }

    public static KeyBinding getBasicSpyToggleKey() {
        return BASIC_SPY_TOGGLE_KEY;
    }

    public static KeyBinding getSpyBookToggleKey() {
        return SPY_BOOK_TOGGLE_KEY;
    }

    public static KeyBinding getSpySignToggleKey() {
        return SPY_SIGN_TOGGLE_KEY;
    }

    public static void toggleSpyChat() {

        isChatSpyVisible = !isChatSpyVisible;
        isBookSpyVisible = isChatSpyVisible;
        isSignSpyVisible = isChatSpyVisible;
    }

    public static void toggleBasicSpy() {
        LOGGER.info("Изменяем обычный чат. Старое значение: {}, новое значение: {}",
                isChatSpyVisible, !isChatSpyVisible);
        isChatSpyVisible = !isChatSpyVisible;
    }

    public static void toggleSpyBook() {
        LOGGER.info("Изменяем SPY-BOOK. Старое значение: {}, новое значение: {}",
                isBookSpyVisible, !isBookSpyVisible);
        isBookSpyVisible = !isBookSpyVisible;
    }

    public static void toggleSpySign() {
        LOGGER.info("Изменяем SPY-SIGN. Старое значение: {}, новое значение: {}",
                isSignSpyVisible, !isSignSpyVisible);
        isSignSpyVisible = !isSignSpyVisible;
    }

    public static boolean isChatSpyVisible() {
        return isChatSpyVisible;
    }

    public static boolean isBookSpyVisible() {
        return isBookSpyVisible;
    }

    public static boolean isSignSpyVisible() {
        return isSignSpyVisible;
    }
}
