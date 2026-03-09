package com.ratger;

import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class SpyFilterConfig {
	private static final String FILE_NAME = "spyfilter.yml";
	private static final String KEY_SPY_CHAT_REGEX = "chatRegex";
	private static final String KEY_SPY_BOOK_CONTAINS = "bookContains";
	private static final String KEY_SPY_SIGN_CONTAINS = "signContains";
	private static final String KEY_SPY_SILENT_CONTAINS = "silentContains";

	private static final String DEFAULT_SPY_CHAT_REGEX = "^SPY:.*?:.*$";
	private static final String DEFAULT_SPY_BOOK_CONTAINS = "[SPY BOOK]";
	private static final String DEFAULT_SPY_SIGN_CONTAINS = "[SPY SIGN]";
	private static final String DEFAULT_SPY_SILENT_CONTAINS = "[Втихомолку]";

	private static Pattern spyChatPattern = Pattern.compile(DEFAULT_SPY_CHAT_REGEX);
	private static String spyBookContains = DEFAULT_SPY_BOOK_CONTAINS;
	private static String spySignContains = DEFAULT_SPY_SIGN_CONTAINS;
	private static String spySilentContains = DEFAULT_SPY_SILENT_CONTAINS;

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

				String loadedSpyChatRegex = readString(configMap, KEY_SPY_CHAT_REGEX);
				String loadedSpyBookContains = readString(configMap, KEY_SPY_BOOK_CONTAINS);
				String loadedSpySignContains = readString(configMap, KEY_SPY_SIGN_CONTAINS);
				String loadedSpySilentContains = readString(configMap, KEY_SPY_SILENT_CONTAINS);

                spyChatPattern = Pattern.compile(loadedSpyChatRegex);
				spyBookContains = loadedSpyBookContains;
				spySignContains = loadedSpySignContains;
				spySilentContains = loadedSpySilentContains;
			}
		} catch (IOException | IllegalArgumentException exception) {
			try {
				writeDefault(configPath);
			} catch (IOException ignored) {
			}
			applyDefaults();
		}
	}

	private static String readString(Map<?, ?> map, String key) {
		Object value = map.get(key);
		if (!(value instanceof String stringValue) || stringValue.isBlank()) {
			throw new IllegalArgumentException("Invalid config value for key: " + key);
		}
		return stringValue;
	}

	private static void writeDefault(Path path) throws IOException {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);
		options.setIndent(2);

		Yaml prettyYaml = new Yaml(options);

		Map<String, String> defaultValues = new LinkedHashMap<>();
		defaultValues.put(KEY_SPY_CHAT_REGEX, DEFAULT_SPY_CHAT_REGEX);
		defaultValues.put(KEY_SPY_BOOK_CONTAINS, DEFAULT_SPY_BOOK_CONTAINS);
		defaultValues.put(KEY_SPY_SIGN_CONTAINS, DEFAULT_SPY_SIGN_CONTAINS);
		defaultValues.put(KEY_SPY_SILENT_CONTAINS, DEFAULT_SPY_SILENT_CONTAINS);

		try (Writer writer = Files.newBufferedWriter(path)) {
			prettyYaml.dump(defaultValues, writer);
		}
	}

	private static void applyDefaults() {
		spyChatPattern = Pattern.compile(DEFAULT_SPY_CHAT_REGEX);
		spyBookContains = DEFAULT_SPY_BOOK_CONTAINS;
		spySignContains = DEFAULT_SPY_SIGN_CONTAINS;
		spySilentContains = DEFAULT_SPY_SILENT_CONTAINS;
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
}
