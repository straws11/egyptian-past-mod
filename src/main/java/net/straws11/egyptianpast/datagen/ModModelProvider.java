package net.straws11.egyptianpast.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.EgyptianPastBlockRegistration;
import net.straws11.egyptianpast.item.EgyptianPastItemRegistration;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, EgyptianPast.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // Items
        itemModels.generateFlatItem(EgyptianPastItemRegistration.EXAMPLE_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(EgyptianPastItemRegistration.PAPYRUS_REED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(EgyptianPastItemRegistration.PAPYRUS_SHEET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(EgyptianPastItemRegistration.MUMMY_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        // add more models here

        // Blocks
        blockModels.createTrivialCube(EgyptianPastBlockRegistration.EXAMPLE_BLOCK.get());
        blockModels.createTrivialCube(EgyptianPastBlockRegistration.LIMESTONE.get());
        // add more models here
    }
}
