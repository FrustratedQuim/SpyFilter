package com.ratger;

import com.ratger.client.SpyFilterKeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.client.resource.language.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class SpyFilterClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger("spyfilter");
	private static final Pattern SPY_MESSAGE_PATTERN = Pattern.compile("^SPY:.*?:.*$");
	private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getSpyChatToggleKey());
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getBasicSpyToggleKey());
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getSpyBookToggleKey());
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getSpySignToggleKey());

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			if (SpyFilterKeyBindings.getSpyChatToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleSpyChat();
				LOGGER.info("SpyChat toggled: isChatSpyVisible={}, isBookSpyVisible={}, isSignSpyVisible={}",
						SpyFilterKeyBindings.isChatSpyVisible(),
						SpyFilterKeyBindings.isBookSpyVisible(),
						SpyFilterKeyBindings.isSignSpyVisible());
				MinecraftClientAudiences.of().audience().sendActionBar(
						MINI_MESSAGE.deserialize(buildStatusMessage())
				);
			}
			if (SpyFilterKeyBindings.getBasicSpyToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleBasicSpy();
				LOGGER.info("BasicSpy toggled: isChatSpyVisible={}, isBookSpyVisible={}, isSignSpyVisible={}",
						SpyFilterKeyBindings.isChatSpyVisible(),
						SpyFilterKeyBindings.isBookSpyVisible(),
						SpyFilterKeyBindings.isSignSpyVisible());
				MinecraftClientAudiences.of().audience().sendActionBar(
						MINI_MESSAGE.deserialize(buildStatusMessage())
				);
			}
			if (SpyFilterKeyBindings.getSpyBookToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleSpyBook();
				LOGGER.info("SpyBook toggled: isChatSpyVisible={}, isBookSpyVisible={}, isSignSpyVisible={}",
						SpyFilterKeyBindings.isChatSpyVisible(),
						SpyFilterKeyBindings.isBookSpyVisible(),
						SpyFilterKeyBindings.isSignSpyVisible());
				MinecraftClientAudiences.of().audience().sendActionBar(
						MINI_MESSAGE.deserialize(buildStatusMessage())
				);
			}
			if (SpyFilterKeyBindings.getSpySignToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleSpySign();
				LOGGER.info("SpySign toggled: isChatSpyVisible={}, isBookSpyVisible={}, isSignSpyVisible={}",
						SpyFilterKeyBindings.isChatSpyVisible(),
						SpyFilterKeyBindings.isBookSpyVisible(),
						SpyFilterKeyBindings.isSignSpyVisible());
				MinecraftClientAudiences.of().audience().sendActionBar(
						MINI_MESSAGE.deserialize(buildStatusMessage())
				);
			}
		});

		ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
			String rawMessage = message.getString();
			boolean isBasicSpy = SPY_MESSAGE_PATTERN.matcher(rawMessage).matches();
			boolean isSpyBook = rawMessage.contains("[SPY BOOK]");
			boolean isSpySign = rawMessage.contains("[SPY SIGN]");
			LOGGER.info("Message received: rawMessage='{}', isBasicSpy={}, isSpyBook={}, isSpySign={}",
					rawMessage, isBasicSpy, isSpyBook, isSpySign);
			LOGGER.info("Filter states: isChatSpyVisible={}, isBookSpyVisible={}, isSignSpyVisible={}",
					SpyFilterKeyBindings.isChatSpyVisible(),
					SpyFilterKeyBindings.isBookSpyVisible(),
					SpyFilterKeyBindings.isSignSpyVisible());

			if (isBasicSpy && !SpyFilterKeyBindings.isChatSpyVisible()) return false;
			if (isSpyBook && !SpyFilterKeyBindings.isBookSpyVisible()) return false;
			return !isSpySign || SpyFilterKeyBindings.isSignSpyVisible();
		});
	}

	private String buildStatusMessage() {
		String prefix = I18n.translate("message.spyfilter.status");
		return "<gold>" + prefix + " " +
				(SpyFilterKeyBindings.isChatSpyVisible()
						? "<dark_green>[<color:#00ff40>Chat</color>]</dark_green> "
						: "<dark_red>[<color:#FF1500>Chat</color>]</dark_red> ") +
				(SpyFilterKeyBindings.isBookSpyVisible()
						? "<dark_green>[<color:#00ff40>Book</color>]</dark_green> "
						: "<dark_red>[<color:#FF1500>Book</color>]</dark_red> ") +
				(SpyFilterKeyBindings.isSignSpyVisible()
						? "<dark_green>[<color:#00ff40>Sign</color>]</dark_green>"
						: "<dark_red>[<color:#FF1500>Sign</color>]</dark_red>");
	}
}
