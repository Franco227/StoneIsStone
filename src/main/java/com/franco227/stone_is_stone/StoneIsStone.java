package com.franco227.stone_is_stone;

import com.franco227.stone_is_stone.config.SISConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class StoneIsStone implements ModInitializer {
	public static final String MOD_ID = "stone-is-stone";
	public static final Logger LOGGER = LoggerFactory.getLogger("StoneIsStone");
	static File configFile = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json").toFile();
	public static final SISConfig CONFIG = SISConfig.loadConfig(configFile);

	@Override
	public void onInitialize() {
		int stone_variants_nb = CONFIG.stone_variants.size();
		String variants = stone_variants_nb != 1 ? "variants" : "variant";

		LOGGER.info("[Stone Is Stone] Adding %d stone %s to recipes.".formatted(stone_variants_nb, variants));
	}
}
