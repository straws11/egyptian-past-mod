package net.straws11.egyptianpast.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.straws11.egyptianpast.EgyptianPast.MOD_ID;

public class EgyptianPastEntityRegistration {
    // Create a Deferred Register to hold Items which will all be registered under the "egyptianpast" namespace
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(MOD_ID);

    /* longer version of below for some reason
    public static final Supplier<EntityType<MummyEntity>> MUMMY_ENTITY =
            ENTITY_TYPES.register(
                    "mummy",
                    () -> EntityType.Builder.of(
                            MummyEntity::new,
                            MobCategory.MONSTER
                    )
                    .sized(2.0f, 1.0f)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath("egyptianpast", "mummy_entity")
                    ))
            );*/

    public static final Supplier<EntityType<MummyEntity>> MUMMY_ENTITY = ENTITY_TYPES.registerEntityType(
                "mummy_entity", MummyEntity::new, MobCategory.MONSTER,
                builder -> builder.sized(2.0f, 1.0f)
        );


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
