package com.franco227.stone_is_stone.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.Item;
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
        List<Item> STONE_VARIANTS = CONFIG.getStoneVariantsItems();

        map.forEach((tag, entries) -> {
            if (tag.equals(Identifier.of("minecraft", "stone_tool_materials")) ||
                tag.equals(Identifier.of("minecraft", "stone_crafting_materials"))) {
                STONE_VARIANTS.forEach((variant) -> {
                    entries.add(new TagGroupLoader.TrackedEntry(TagEntry.create(Identifier.of(variant.toString())), MOD_ID));
                });
            }
        });
    }
}
