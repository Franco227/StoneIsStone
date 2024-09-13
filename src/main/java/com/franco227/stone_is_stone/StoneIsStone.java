package com.franco227.stone_is_stone;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoneIsStone implements ModInitializer {
	public static final String MOD_ID = "stone-is-stone";

	public static final Logger LOGGER = LoggerFactory.getLogger("StoneIsStone");

	@Override
	public void onInitialize() {
		LOGGER.info("[Stone Is Stone] Adding more stone variants to recipes.");
	}
}
