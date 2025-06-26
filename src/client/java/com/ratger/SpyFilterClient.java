package com.ratger;

import com.ratger.client.SpyFilterKeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;

import java.util.regex.Pattern;
import java.util.Set;

public class SpyFilterClient implements ClientModInitializer {
	private static final Pattern SPY_MESSAGE_PATTERN = Pattern.compile("^SPY:.*?:.*$");
	private static final Set<String> SPY_PREFIXES = Set.of("[SPY BOOK]", "[SPY SIGN]");
	private static final Text SPY_ON_MESSAGE = Text.literal("§aSPY-чат включён");
	private static final Text SPY_OFF_MESSAGE = Text.literal("§cSPY-чат отключён");

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getSpyChatToggleKey());

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (SpyFilterKeyBindings.getSpyChatToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleSpyChat();
				if (client.player != null) {
					client.player.sendMessage(
							SpyFilterKeyBindings.isSpyChatVisible() ? SPY_ON_MESSAGE : SPY_OFF_MESSAGE,
							false
					);
				}
			}
		});

		ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
			String rawMessage = message.getString();
			boolean isBasicSpy = SPY_MESSAGE_PATTERN.matcher(rawMessage).matches();
			boolean isSpecialSpy = !isBasicSpy && SPY_PREFIXES.stream().anyMatch(rawMessage::contains);

			return !(SpyFilterKeyBindings.isSpyChatVisible() == false && (isBasicSpy || isSpecialSpy));
		});
	}
}
