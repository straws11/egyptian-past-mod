package net.straws11.egyptianpast.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.ComponentContents;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.CanopicJarBlock;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.block.PomegranateCropBlock;
import net.straws11.egyptianpast.block.Sarcophagus;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.ModItems;
import net.straws11.egyptianpast.item.OrganType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, EgyptianPast.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // Items
        itemModels.generateFlatItem(ModItems.POMEGRANATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PAPYRUS_SHEET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModBlocks.PAPYRUS_REED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.MUMMY_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PHARAOH_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ANKH_OF_LIFE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.MUMMY_WRAP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.KEY_FRAGMENT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CRYPT_KEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PHARAOH_CROWN.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.LIVER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.LUNGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.STOMACH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.INTESTINES.get(), ModelTemplates.FLAT_ITEM);

        itemModels.itemModelOutput.accept(
            ModBlocks.CANOPIC_JAR.get(),
            new SelectItemModel.Unbaked(
                Optional.empty(),
                new SelectItemModel.UnbakedSwitch<>(
                    new ComponentContents<>(ModDataComponentRegistration.ORGAN_TYPE.get()),
                    // this was a List.of() with duplicated stuff, this is better for this case at least
                    Arrays.stream(OrganType.values()).map(organ ->
                        new SelectItemModel.SwitchCase<>(
                            List.of(organ),
                            new CuboidItemModelWrapper.Unbaked(
                                Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID,
                                    "block/canopic_jar_" + organ.getSerializedName()),
                                Optional.empty(),
                                Collections.emptyList()
                            )
                        )
                    ).collect(Collectors.toList())
                ),
                Optional.of(
                    new CuboidItemModelWrapper.Unbaked(
                        Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "item/canopic_jar_empty"),
                        Optional.empty(),
                        Collections.emptyList()
                    )
                )
            )
        );

        // Blocks
        blockModels.createTrivialCube(ModBlocks.LIMESTONE.get());
        blockModels.createTrivialCube(ModBlocks.EGYPTIAN_STONE.get());
        blockModels.createTrivialCube(ModBlocks.EGYPTIAN_COBBLESTONE.get());
        createSarcophagus(blockModels);
        blockModels.createNonTemplateModelBlock(ModBlocks.PEDESTAL_BLOCK.get());

        blockModels.createCropBlock(ModBlocks.POMEGRANATE_CROP.get(), PomegranateCropBlock.AGE, 0, 1, 2, 3);
        blockModels.createCrossBlock(ModBlocks.PAPYRUS_REED_BLOCK.get(), PlantType.NOT_TINTED);

        createCanopicJar(blockModels);

    }

    public void createCanopicJar(BlockModelGenerators blockModels) {
        // helper to get location for each enum member
        Function<OrganType, MultiVariant> genResourceLocation = (organ) ->
            plainVariant(ModelLocationUtils.getModelLocation(ModBlocks.CANOPIC_JAR_BLOCK.get(),
                "_" + organ.getSerializedName()));

        var organDispatch = PropertyDispatch.initial(CanopicJarBlock.ORGAN);

        for (OrganType organ : OrganType.values()) {
            organDispatch.select(organ, genResourceLocation.apply(organ));
        }

        var facingDispatch = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
            .select(Direction.NORTH, NOP)
            .select(Direction.EAST, Y_ROT_90)
            .select(Direction.SOUTH, Y_ROT_180)
            .select(Direction.WEST, Y_ROT_270);

        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(
                ModBlocks.CANOPIC_JAR_BLOCK.get())
                .with(organDispatch)
                .with(facingDispatch)
        );

    }

    public void createSarcophagus(BlockModelGenerators blockModels) {
        MultiVariant footClosed = plainVariant(ModelLocationUtils.getModelLocation(ModBlocks.SARCOPHAGUS.get(), "_foot"));
        MultiVariant footOpen = plainVariant(ModelLocationUtils.getModelLocation(ModBlocks.SARCOPHAGUS.get(), "_foot_open"));
        MultiVariant headClosed = plainVariant(ModelLocationUtils.getModelLocation(ModBlocks.SARCOPHAGUS.get(), "_head"));
        MultiVariant headOpen = plainVariant(ModelLocationUtils.getModelLocation(ModBlocks.SARCOPHAGUS.get(), "_head_open"));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(ModBlocks.SARCOPHAGUS.get())
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
