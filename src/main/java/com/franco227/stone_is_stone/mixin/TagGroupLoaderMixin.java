package com.franco227.stone_is_stone.mixin;

import java.util.List;
import java.util.Map;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;

import static com.franco227.stone_is_stone.StoneIsStone.CONFIG;
import static com.franco227.stone_is_stone.StoneIsStone.MOD_ID;

@Mixin(TagLoader.class)
public class TagGroupLoaderMixin {

    @Unique
    private boolean isEqualToVanillaTag(ResourceLocation tag, String vanilla_tag) {
        return tag.equals(ResourceLocation.fromNamespaceAndPath("minecraft", vanilla_tag));
    }

    @Inject(method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;)Ljava/util/Map;", at = @At("RETURN"))
    public void interceptLoadTags(
            ResourceManager resourceManager,
            CallbackInfoReturnable<Map<ResourceLocation, List<TagLoader.EntryWithSource>>> cir,
            @Local Map<ResourceLocation, List<TagLoader.EntryWithSource>> map
    ) {
        List<Item> STONE_VARIANTS = CONFIG.getStoneVariantsItems();

        map.forEach((tag, entries) -> {
            if (isEqualToVanillaTag(tag, "stone_tool_materials") || isEqualToVanillaTag(tag, "stone_crafting_materials")) {
                STONE_VARIANTS.forEach((variant) -> entries.add(new TagLoader.EntryWithSource(TagEntry.element(ResourceLocation.parse(variant.toString())), MOD_ID)));
            }
        });
    }
}
