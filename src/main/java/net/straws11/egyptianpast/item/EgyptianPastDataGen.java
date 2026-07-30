package net.straws11.egyptianpast.item;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.datagen.ModModelProvider;

@EventBusSubscriber(modid = EgyptianPast.MOD_ID)
public class EgyptianPastDataGen {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new ModModelProvider(packOutput));
    }
}
