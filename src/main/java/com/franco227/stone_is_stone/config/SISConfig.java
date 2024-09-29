package com.franco227.stone_is_stone.config;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.include.com.google.gson.Gson;
import org.spongepowered.include.com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.franco227.stone_is_stone.StoneIsStone.LOGGER;

public class SISConfig {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public String version = "1";

    public List<String> stone_variants = List.of(
            "minecraft:andesite",
            "minecraft:diorite",
            "minecraft:granite",
            "minecraft:end_stone",
            "minecraft:stone",
            "minecraft:deepslate",
            "minecraft:mossy_cobblestone"
    );


    public List<Item> getStoneVariantsItems() {
        List<Item> STONE_VARIANTS_ITEMS = new java.util.ArrayList<>(List.of());
        this.stone_variants.forEach((variant) -> STONE_VARIANTS_ITEMS.add(Registries.ITEM.get(Identifier.of(variant))));
        return STONE_VARIANTS_ITEMS;
    }


    public static SISConfig loadConfig(File file) {
        SISConfig config = null;

        if (file.exists()) {
            try (
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))
            ) {
                config = gson.fromJson(fileReader, SISConfig.class);
            } catch (org.spongepowered.include.com.google.gson.JsonSyntaxException e) {
                String error_message = "[Stone Is Stone] Failed to load config: %s".formatted(e.getMessage().split(": ")[1]);
                LOGGER.error(error_message);
            } catch (Exception e) {
                LOGGER.error("[Stone Is Stone] Failed to load config: ", e);
            }
        }

        if (config == null) {
            LOGGER.info("[Stone Is Stone] Recreating config.");
            config = new SISConfig();
        }

        config.saveConfig(file);
        String loaded_message = "[Stone Is Stone] Loaded config! (config v%s)".formatted(config.version);
        LOGGER.info(loaded_message);
        return config;
    }


    public void saveConfig(File file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            LOGGER.error("[Stone Is Stone] Failed to save config: ", e);
        }
    }
}
