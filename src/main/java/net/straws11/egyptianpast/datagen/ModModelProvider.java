package net.straws11.egyptianpast.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.ModBlockRegistration;
import net.straws11.egyptianpast.item.ModItemRegistration;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, EgyptianPast.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // Items
        itemModels.generateFlatItem(ModItemRegistration.POMEGRANATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.PAPYRUS_SHEET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModBlockRegistration.PAPYRUS_REED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.MUMMY_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.ANKH_OF_LIFE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.MUMMY_WRAP.get(), ModelTemplates.FLAT_ITEM);
        // add more models here

        // Blocks
        blockModels.createTrivialCube(ModBlockRegistration.LIMESTONE.get());
        blockModels.createTrivialCube(ModBlockRegistration.PAPYRUS_REED_BLOCK.get());
        // add more models here

    }
}
