package com.ratger;

import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

public final class SpyFilterConfig {
	private static final String FILE_NAME = "spyfilter.yml";
	private static final String KEY_SPY_CHAT_REGEX = "chatRegex";
	private static final String KEY_SPY_BOOK_CONTAINS = "bookContains";
	private static final String KEY_SPY_SIGN_CONTAINS = "signContains";
	private static final String KEY_SPY_SILENT_CONTAINS = "silentContains";
	private static final String KEY_CHAT_NAMED = "chatNamed";
	private static final String KEY_BOOK_NAMED = "bookNamed";
	private static final String KEY_SIGN_NAMED = "signNamed";
	private static final String KEY_SILENT_NAMED = "silentNamed";

	private static final String DEFAULT_SPY_CHAT_REGEX = "^SPY:.*?:.*$";
	private static final String DEFAULT_SPY_BOOK_CONTAINS = "[SPY BOOK]";
	private static final String DEFAULT_SPY_SIGN_CONTAINS = "[SPY SIGN]";
	private static final String DEFAULT_SPY_SILENT_CONTAINS = "[Втихомолку]";
	private static final String DEFAULT_CHAT_NAMED = "Chat";
	private static final String DEFAULT_BOOK_NAMED = "Book";
	private static final String DEFAULT_SIGN_NAMED = "Sign";
	private static final String DEFAULT_SILENT_NAMED = "Silent";

	private static Pattern spyChatPattern = Pattern.compile(DEFAULT_SPY_CHAT_REGEX);
	private static String spyBookContains = DEFAULT_SPY_BOOK_CONTAINS;
	private static String spySignContains = DEFAULT_SPY_SIGN_CONTAINS;
	private static String spySilentContains = DEFAULT_SPY_SILENT_CONTAINS;
	private static String chatNamed = DEFAULT_CHAT_NAMED;
	private static String bookNamed = DEFAULT_BOOK_NAMED;
	private static String signNamed = DEFAULT_SIGN_NAMED;
	private static String silentNamed = DEFAULT_SILENT_NAMED;

	private SpyFilterConfig() {
	}

	public static void load() {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
		Yaml yaml = new Yaml();

		try {
			Files.createDirectories(configPath.getParent());
			if (!Files.exists(configPath)) {
				writeDefault(configPath);
				applyDefaults();
				return;
			}

			try (InputStream inputStream = Files.newInputStream(configPath)) {
				Object loaded = yaml.load(inputStream);
				if (!(loaded instanceof Map<?, ?> configMap)) {
					writeDefault(configPath);
					applyDefaults();
					return;
				}

				String loadedSpyChatRegex = readRequiredString(configMap, KEY_SPY_CHAT_REGEX);
				String loadedSpyBookContains = readRequiredString(configMap, KEY_SPY_BOOK_CONTAINS);
				String loadedSpySignContains = readRequiredString(configMap, KEY_SPY_SIGN_CONTAINS);
				String loadedSpySilentContains = readRequiredString(configMap, KEY_SPY_SILENT_CONTAINS);
				String loadedChatNamed = readOptionalString(configMap, KEY_CHAT_NAMED, DEFAULT_CHAT_NAMED);
				String loadedBookNamed = readOptionalString(configMap, KEY_BOOK_NAMED, DEFAULT_BOOK_NAMED);
				String loadedSignNamed = readOptionalString(configMap, KEY_SIGN_NAMED, DEFAULT_SIGN_NAMED);
				String loadedSilentNamed = readOptionalString(configMap, KEY_SILENT_NAMED, DEFAULT_SILENT_NAMED);

				spyChatPattern = Pattern.compile(loadedSpyChatRegex);
				spyBookContains = loadedSpyBookContains;
				spySignContains = loadedSpySignContains;
				spySilentContains = loadedSpySilentContains;
				chatNamed = loadedChatNamed;
				bookNamed = loadedBookNamed;
				signNamed = loadedSignNamed;
				silentNamed = loadedSilentNamed;
			}
		} catch (IOException | IllegalArgumentException exception) {
			try {
				writeDefault(configPath);
			} catch (IOException ignored) {
			}
			applyDefaults();
		}
	}

	private static String readRequiredString(Map<?, ?> map, String key) {
		Object value = map.get(key);
		if (!(value instanceof String stringValue) || stringValue.isBlank()) {
			throw new IllegalArgumentException("Invalid config value for key: " + key);
		}
		return stringValue;
	}

	private static String readOptionalString(Map<?, ?> map, String key, String defaultValue) {
		Object value = map.get(key);
		if (value == null) {
			return defaultValue;
		}
		if (!(value instanceof String stringValue) || stringValue.isBlank()) {
			throw new IllegalArgumentException("Invalid config value for key: " + key);
		}
		return stringValue;
	}

	private static void writeDefault(Path path) throws IOException {
		String content = String.join("\n",
				KEY_SPY_CHAT_REGEX + ": '" + escapeSingleQuotes(DEFAULT_SPY_CHAT_REGEX) + "'",
				KEY_SPY_BOOK_CONTAINS + ": '" + escapeSingleQuotes(DEFAULT_SPY_BOOK_CONTAINS) + "'",
				KEY_SPY_SIGN_CONTAINS + ": '" + escapeSingleQuotes(DEFAULT_SPY_SIGN_CONTAINS) + "'",
				KEY_SPY_SILENT_CONTAINS + ": '" + escapeSingleQuotes(DEFAULT_SPY_SILENT_CONTAINS) + "'",
				"",
				KEY_CHAT_NAMED + ": '" + escapeSingleQuotes(DEFAULT_CHAT_NAMED) + "'",
				KEY_BOOK_NAMED + ": '" + escapeSingleQuotes(DEFAULT_BOOK_NAMED) + "'",
				KEY_SIGN_NAMED + ": '" + escapeSingleQuotes(DEFAULT_SIGN_NAMED) + "'",
				KEY_SILENT_NAMED + ": '" + escapeSingleQuotes(DEFAULT_SILENT_NAMED) + "'",
				""
		);
		Files.writeString(path, content, StandardCharsets.UTF_8);
	}

	private static String escapeSingleQuotes(String value) {
		return value.replace("'", "''");
	}

	private static void applyDefaults() {
		spyChatPattern = Pattern.compile(DEFAULT_SPY_CHAT_REGEX);
		spyBookContains = DEFAULT_SPY_BOOK_CONTAINS;
		spySignContains = DEFAULT_SPY_SIGN_CONTAINS;
		spySilentContains = DEFAULT_SPY_SILENT_CONTAINS;
		chatNamed = DEFAULT_CHAT_NAMED;
		bookNamed = DEFAULT_BOOK_NAMED;
		signNamed = DEFAULT_SIGN_NAMED;
		silentNamed = DEFAULT_SILENT_NAMED;
	}

	public static Pattern getSpyChatPattern() {
		return spyChatPattern;
	}

	public static String getSpyBookContains() {
		return spyBookContains;
	}

	public static String getSpySignContains() {
		return spySignContains;
	}

	public static String getSpySilentContains() {
		return spySilentContains;
	}

	public static String getChatNamed() {
		return chatNamed;
	}

	public static String getBookNamed() {
		return bookNamed;
	}

	public static String getSignNamed() {
		return signNamed;
	}

	public static String getSilentNamed() {
		return silentNamed;
	}
}
