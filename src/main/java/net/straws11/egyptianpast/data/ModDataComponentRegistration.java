package net.straws11.egyptianpast.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.StringRepresentable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.item.OrganType;
import net.straws11.egyptianpast.item.ScrollType;

public class ModDataComponentRegistration {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, EgyptianPast.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<OrganType>> ORGAN_TYPE =
         DATA_COMPONENT_TYPES.register("organ_type", () ->
                 DataComponentType.<OrganType>builder()
                         .persistent(StringRepresentable.fromEnum(OrganType::values))
                         //.networkSynchronized()
                         .build()
                 );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IS_CURSED =
            DATA_COMPONENT_TYPES.register("is_cursed", () ->
                    DataComponentType.<Boolean>builder()
                            .persistent(Codec.BOOL)
                            .build()
                    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ScrollType>> SCROLL_TYPE =
        DATA_COMPONENT_TYPES.register("scroll_type", () ->
            DataComponentType.<ScrollType>builder()
                .persistent(StringRepresentable.fromEnum(ScrollType::values))
                .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> STORED_TOTEMS =
        DATA_COMPONENT_TYPES.register("stored_totems", () ->
            DataComponentType.<Integer>builder()
                .persistent(Codec.INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .build()
        );

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
