package net.straws11.egyptianpast;

import com.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.straws11.egyptianpast.block.entity.ModBlockEntities;
import net.straws11.egyptianpast.block.entity.renderer.PedestalBlockEntityRenderer;
import net.straws11.egyptianpast.entity.ModEntities;
import net.straws11.egyptianpast.renderer.entity.PharaohRenderer;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = EgyptianPast.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = EgyptianPast.MOD_ID, value = Dist.CLIENT)
public class EgyptianPastClient {
    public EgyptianPastClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        EgyptianPast.LOGGER.info("HELLO FROM CLIENT SETUP");
        EgyptianPast.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.MUMMY_ENTITY.get(),
                context -> new GeoEntityRenderer<>(context, ModEntities.MUMMY_ENTITY.get())
        );

        event.registerEntityRenderer(
                ModEntities.PHARAOH_ENTITY.get(),
                PharaohRenderer::new
        );
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.PEDESTAL_BE.get(),
            PedestalBlockEntityRenderer::new);
    }

    public static Identifier getEntityTexture(String image) {
        return Identifier.fromNamespaceAndPath("egyptianpast", "textures/entity/" + image);
    }
}
