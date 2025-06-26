package com.ratger;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpyFilter implements ModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger("spyfilter");

	@Override
	public void onInitialize() {
		LOGGER.info("SpyFilter загружен!");
	}
}
