package net.straws11.egyptianpast.item;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.util.GeckoLibUtil;
import com.google.common.base.Suppliers;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PharaohCrown extends Item implements ICursedItem, GeoItem {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public PharaohCrown(Properties properties) {
        super(properties.component(ModDataComponentRegistration.IS_CURSED.get(), true));
    }

    @Override
    public @NonNull ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        // prevent cursed items from having armor ratings
        if (isCursed(stack)) {
            return ItemAttributeModifiers.EMPTY;
        }
        return super.getDefaultAttributeModifiers(stack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        appendCursedTooltip(itemStack, context, display, builder, tooltipFlag);
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return super.isFoil(itemStack);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private final Supplier<GeoArmorRenderer<PharaohCrown, HumanoidRenderState>> renderer =
                Suppliers.memoize(() -> new GeoArmorRenderer<>(PharaohCrown.this));

            @Override
            public @Nullable GeoArmorRenderer<?, ?> getGeoArmorRenderer(ItemStack itemStack, EquipmentSlot equipmentSlot) {
                return this.renderer.get();
            }
        });
    }
}
