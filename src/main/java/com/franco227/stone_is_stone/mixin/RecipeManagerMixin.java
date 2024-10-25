package com.franco227.stone_is_stone.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Unique
    private boolean mcIsWithinVersions(Version mcVersion, String minVersionString, String maxVersionString) {
        Version minVersion;
        Version maxVersion;
        try {
            minVersion = Version.parse(minVersionString);
            maxVersion = Version.parse(maxVersionString);
        } catch (VersionParsingException e) {
            throw new RuntimeException(e);
        }
        return mcVersion.compareTo(minVersion) >= 0 && mcVersion.compareTo(maxVersion) <= 0;
    }

    @Unique
    private void replaceCobblestoneIngredient(JsonObject currentKey) {
        FabricLoader.getInstance().getModContainer("minecraft").ifPresent((minecraft) -> {
            Version mcVersion = minecraft.getMetadata().getVersion();

            if (mcIsWithinVersions(mcVersion, "1.19.3", "1.21.1")) {
                if (currentKey.get("item") != null && currentKey.get("item").getAsString().equals("minecraft:cobblestone")) {
                    currentKey.remove("item");
                    currentKey.addProperty("tag", "minecraft:stone_crafting_materials");
                }
            }
        });
    }

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        List<String> recipesToOverride = List.of(
            "minecraft:dispenser",
            "minecraft:dropper",
            "minecraft:lever",
            "minecraft:observer",
            "minecraft:piston"
        );

        map.forEach((identifier, jsonElement) -> {
            if (recipesToOverride.contains(identifier.toString())) {
                JsonObject recipe = jsonElement.getAsJsonObject();
                if (recipe.get("type").getAsString().equals("minecraft:crafting_shaped")) {
                    JsonObject key = recipe.get("key").getAsJsonObject();
                    key.keySet().forEach((keyName) -> replaceCobblestoneIngredient(key.get(keyName).getAsJsonObject()));
                }
            }
        });
    }
}
