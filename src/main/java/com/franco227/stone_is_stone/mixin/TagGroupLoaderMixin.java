package com.franco227.stone_is_stone.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

import static com.franco227.stone_is_stone.StoneIsStone.CONFIG;
import static com.franco227.stone_is_stone.StoneIsStone.MOD_ID;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin {

    @Inject(method = "loadTags(Lnet/minecraft/resource/ResourceManager;)Ljava/util/Map;", at = @At("RETURN"))
    public void interceptLoadTags(
        ResourceManager resourceManager,
        CallbackInfoReturnable<Map<Identifier, List<TagGroupLoader.TrackedEntry>>> cir,
        @Local Map<Identifier, List<TagGroupLoader.TrackedEntry>> map
    ) {
        List<String> stoneVariants = CONFIG.stone_variants;
        List<String> stoneTags = List.of(
            "minecraft:stone_tool_materials",
            "minecraft:stone_crafting_materials"
        );

        map.forEach((tag, entries) -> {
            if (stoneTags.contains(tag.toString())) {
                stoneVariants.forEach((variant) -> entries.add(new TagGroupLoader.TrackedEntry(TagEntry.create(Identifier.of(variant)), MOD_ID)));
            }
        });
    }
}
