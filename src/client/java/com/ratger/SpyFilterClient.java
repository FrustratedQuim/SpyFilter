package com.ratger;

import com.ratger.client.SpyFilterKeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.client.resource.language.I18n;


public class SpyFilterClient implements ClientModInitializer {
	private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getSpyChatToggleKey());
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getBasicSpyToggleKey());
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getSpyBookToggleKey());
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getSpySignToggleKey());
		KeyBindingHelper.registerKeyBinding(SpyFilterKeyBindings.getSpySilentToggleKey());

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			if (SpyFilterKeyBindings.getSpyChatToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleSpyChat();
				MinecraftClientAudiences.of().audience().sendActionBar(
						MINI_MESSAGE.deserialize(buildStatusMessage())
				);
			}
			if (SpyFilterKeyBindings.getBasicSpyToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleBasicSpy();
				MinecraftClientAudiences.of().audience().sendActionBar(
						MINI_MESSAGE.deserialize(buildStatusMessage())
				);
			}
			if (SpyFilterKeyBindings.getSpyBookToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleSpyBook();
				MinecraftClientAudiences.of().audience().sendActionBar(
						MINI_MESSAGE.deserialize(buildStatusMessage())
				);
			}
			if (SpyFilterKeyBindings.getSpySignToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleSpySign();
				MinecraftClientAudiences.of().audience().sendActionBar(
						MINI_MESSAGE.deserialize(buildStatusMessage())
				);
			}
			if (SpyFilterKeyBindings.getSpySilentToggleKey().wasPressed()) {
				SpyFilterKeyBindings.toggleSpySilent();
				MinecraftClientAudiences.of().audience().sendActionBar(
						MINI_MESSAGE.deserialize(buildStatusMessage())
				);
			}
		});

		ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
			String rawMessage = message.getString();
			boolean isBasicSpy = SpyFilterConfig.getSpyChatPattern().matcher(rawMessage).matches();
			boolean isSpyBook = rawMessage.contains(SpyFilterConfig.getSpyBookContains());
			boolean isSpySign = rawMessage.contains(SpyFilterConfig.getSpySignContains());
			boolean isSpySilent = rawMessage.contains(SpyFilterConfig.getSpySilentContains());

			if (isBasicSpy && !SpyFilterKeyBindings.isChatSpyVisible()) return false;
			if (isSpyBook && !SpyFilterKeyBindings.isBookSpyVisible()) return false;
			if (isSpySilent && !SpyFilterKeyBindings.isSilentSpyVisible()) return false;
			return !isSpySign || SpyFilterKeyBindings.isSignSpyVisible();
		});
	}

	private String buildStatusMessage() {
		String prefix = I18n.translate("message.spyfilter.status");
		return "<gold>" + prefix + " " +
				(SpyFilterKeyBindings.isChatSpyVisible()
						? "<dark_green>[<color:#00ff40>" + SpyFilterConfig.getChatNamed() + "</color>]</dark_green> "
						: "<dark_red>[<color:#FF1500>" + SpyFilterConfig.getChatNamed() + "</color>]</dark_red> ") +
				(SpyFilterKeyBindings.isBookSpyVisible()
						? "<dark_green>[<color:#00ff40>" + SpyFilterConfig.getBookNamed() + "</color>]</dark_green> "
						: "<dark_red>[<color:#FF1500>" + SpyFilterConfig.getBookNamed() + "</color>]</dark_red> ") +
				(SpyFilterKeyBindings.isSignSpyVisible()
						? "<dark_green>[<color:#00ff40>" + SpyFilterConfig.getSignNamed() + "</color>]</dark_green> "
						: "<dark_red>[<color:#FF1500>" + SpyFilterConfig.getSignNamed() + "</color>]</dark_red> ") +
				(SpyFilterKeyBindings.isSilentSpyVisible()
				? "<dark_green>[<color:#00ff40>" + SpyFilterConfig.getSilentNamed() + "</color>]</dark_green>"
				: "<dark_red>[<color:#FF1500>" + SpyFilterConfig.getSilentNamed() + "</color>]</dark_red>");
	}
}
