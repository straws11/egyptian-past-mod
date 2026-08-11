package net.straws11.egyptianpast.data;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.StringRepresentable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.item.OrganType;

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

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
