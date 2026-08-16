package net.straws11.egyptianpast.villager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagEntry;
import net.straws11.egyptianpast.EgyptianPast;

import java.util.concurrent.CompletableFuture;

public class ModPOITags extends PoiTypeTagsProvider {
    public ModPOITags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, EgyptianPast.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(PoiTypeTags.ACQUIRABLE_JOB_SITE)
            .add(TagEntry.element(ModVillagers.EGYPTIAN_VILLAGER_POI.unwrapKey().get().identifier()));
    }
}
