package net.straws11.egyptianpast.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.ModBlockRegistration;
import net.straws11.egyptianpast.block.Sarcophagus;
import net.straws11.egyptianpast.item.ModItemRegistration;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

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
        itemModels.generateFlatItem(ModItemRegistration.PHARAOH_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.ANKH_OF_LIFE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.MUMMY_WRAP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.KEY_FRAGMENT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.CRYPT_KEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.PHARAOH_CROWN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItemRegistration.CANOPIC_JAR.get(), ModelTemplates.FLAT_ITEM);

        // Blocks
        blockModels.createTrivialCube(ModBlockRegistration.LIMESTONE.get());
        blockModels.createTrivialCube(ModBlockRegistration.EGYPTIAN_STONE.get());
        blockModels.createTrivialCube(ModBlockRegistration.EGYPTIAN_COBBLESTONE.get());
        createSarcophagus(blockModels);
        blockModels.createTrivialCube(ModBlockRegistration.PAPYRUS_REED_BLOCK.get());
        // TODO: this needs to change
        blockModels.createTrivialCube(ModBlockRegistration.PEDESTAL_BLOCK.get());

    }

    public void createSarcophagus(BlockModelGenerators blockModels) {
        MultiVariant footClosed = plainVariant(ModelLocationUtils.getModelLocation(ModBlockRegistration.SARCOPHAGUS.get(), "_foot"));
        MultiVariant footOpen = plainVariant(ModelLocationUtils.getModelLocation(ModBlockRegistration.SARCOPHAGUS.get(), "_foot_open"));
        MultiVariant headClosed = plainVariant(ModelLocationUtils.getModelLocation(ModBlockRegistration.SARCOPHAGUS.get(), "_head"));
        MultiVariant headOpen = plainVariant(ModelLocationUtils.getModelLocation(ModBlockRegistration.SARCOPHAGUS.get(), "_head_open"));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(ModBlockRegistration.SARCOPHAGUS.get())
                        .with(PropertyDispatch.initial(Sarcophagus.OPENED, BlockStateProperties.BED_PART)
                                .select(false, BedPart.FOOT, footClosed)
                                .select(true, BedPart.FOOT, footOpen)
                                .select(false, BedPart.HEAD, headClosed)
                                .select(true, BedPart.HEAD, headOpen)
                        )
                        .with(PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
                                .select(Direction.NORTH, NOP)
                                .select(Direction.EAST, Y_ROT_90)
                                .select(Direction.SOUTH, Y_ROT_180)
                                .select(Direction.WEST, Y_ROT_270)
                        )
        );
    }
}
